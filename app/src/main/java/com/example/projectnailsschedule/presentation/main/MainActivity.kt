package com.example.projectnailsschedule.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.delay
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()
    private var mainViewModel: MainViewModel? = null

    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    private fun showInContextUI() {
        Log.e("Permission", "showUI")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permission() {
        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher. You can use either a val, as shown in this snippet,
        // or a lateinit var in your onAttach() or onCreate() method.
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.e("Permission", "Granted")
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.e("Permission", "Not Granted")
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                showInContextUI()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        permission()

        // Set uncaught exception handler
        uncaughtExceptionHandler.context = this
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        // Create work folders
        WorkFolders().run()

        super.onCreate(savedInstanceState)

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
        }
        return super.onOptionsItemSelected(item)
    }
}