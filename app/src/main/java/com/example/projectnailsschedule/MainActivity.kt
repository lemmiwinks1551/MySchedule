package com.example.projectnailsschedule

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectnailsschedule.databinding.ActivityMainBinding
import com.example.projectnailsschedule.service.UncaughtExceptionHandler
import com.example.projectnailsschedule.service.Service
import com.example.projectnailsschedule.service.WorkFolders
import com.example.projectnailsschedule.ui.calendar.CalendarFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()


    override fun onCreate(savedInstanceState: Bundle?) {
        // Set uncaught exception handler
        uncaughtExceptionHandler.context = this
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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
        if (WorkFolders().state == Thread.State.NEW) {
            WorkFolders().start()
        }

        // Set click listener on navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Set toolbar with specific name of week day
            if (destination.id == R.id.nav_date) {
                // Get selected date
                val checkedDate = CalendarFragment().getSelectedDate()
                // Convert Date string to Local Date
                val weekDay = Service().stringToLocalDate(checkedDate)
                // Set String into toolbar
                binding.appBarMain.toolbar.title =
                    "${Service().getWeekDayName(weekDay, this)} $checkedDate"
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> navController.navigate(R.id.nav_search)
        }
        return super.onOptionsItemSelected(item)
    }
}