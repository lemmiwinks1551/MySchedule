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
import com.example.projectnailsschedule.util.UncaughtExceptionHandler
import com.example.projectnailsschedule.util.WorkFolders
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()
    private var mainViewModel: MainViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        // Set uncaught exception handler
        uncaughtExceptionHandler.context = this
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        mainViewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(this)
        )[MainViewModel::class.java]

        mainViewModel?.loadTheme()
        loadTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_content_main)

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
        navView.setupWithNavController(navController)

        // Create work folders
        WorkFolders().run()
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

    private fun loadTheme() {
        if (mainViewModel?.darkThemeOn!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> navController.navigate(R.id.nav_search)
        }
        return super.onOptionsItemSelected(item)
    }
}