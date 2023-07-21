package com.example.projectnailsschedule.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.ActivityMainBinding
import com.example.projectnailsschedule.domain.models.FirebaseModel
import com.example.projectnailsschedule.util.FirebaseMetrics
import com.example.projectnailsschedule.util.UncaughtExceptionHandler
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.example.projectnailsschedule.util.rustore.RuStoreReview
import com.example.projectnailsschedule.util.rustore.RuStoreUpdate
import com.google.android.material.navigation.NavigationView
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private val log = this::class.simpleName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()
    private var mainViewModel: MainViewModel? = null

    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Set uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        // create ViewModel object with Factory
        mainViewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(this)
        )[MainViewModel::class.java]

        // set dark/light theme
        mainViewModel?.loadTheme()
        setTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // init all widgets
        initWidgets()

        // configure app bar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_calendar,
                R.id.nav_clients,
                R.id.nav_price,
                R.id.nav_settings,
                R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)

        // check for updates
        RuStoreUpdate(this).checkForUpdates()

        // start advertising
        RuStoreAd(this, binding).interstitialAd()

        // request user's review
        RuStoreReview(this).rateApp()

        // insert metrics
        val firebaseModel = FirebaseModel(
            time = LocalDateTime.now(),
            event = "Start"
        )
        FirebaseMetrics().insertMetrics(firebaseModel)
    }

    private fun initWidgets() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setTheme() {
        // change theme if it changed
        var actualDarkThemeOn: Boolean? = null

        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                actualDarkThemeOn = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                actualDarkThemeOn = false
            }
        }

        if (!mainViewModel?.darkThemeOn!! && actualDarkThemeOn == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (mainViewModel?.darkThemeOn!! && actualDarkThemeOn == false) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> navController.navigate(R.id.nav_search)
            R.id.full_month_view -> navController.navigate(R.id.fullMonthViewFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        RuStoreAd(this, binding).destroyAd()
        super.onDestroy()
    }
}