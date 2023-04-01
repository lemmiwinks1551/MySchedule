package com.example.projectnailsschedule.presentation.main

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
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
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()
    private var mainViewModel: MainViewModel? = null

    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    private var permissionGranted = false

    private fun closeApp() {
        finish()
        exitProcess(0)
    }

    private fun showInContextUI() {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permission() {

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }

        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // permission already granted
            }
            shouldShowRequestPermissionRationale(
                // show request permission dialog
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            -> {
                // if permission already denied
                showInContextUI()
            }
            else -> {
                // Ask for the permission.
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("asd", "a")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        permission()

        // Set uncaught exception handler
        uncaughtExceptionHandler.context = this
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        // Create work folders
         WorkFolders().run()

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