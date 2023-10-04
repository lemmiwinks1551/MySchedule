package com.example.projectnailsschedule.presentation.main

import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.example.projectnailsschedule.util.rustore.RuStoreReview
import com.example.projectnailsschedule.util.rustore.RuStoreUpdate
import com.google.android.material.navigation.NavigationView
import com.my.target.ads.MyTargetView

class MainActivity : AppCompatActivity() {
    private val log = this::class.simpleName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()
    private var mainViewModel: MainViewModel? = null

    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    private val ruStoreAd = RuStoreAd()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Set uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        // create ViewModel object with Factory
        mainViewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(this)
        )[MainViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // init all widgets
        initWidgets()

        // configure app bar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_calendar,
                R.id.nav_clients,
                R.id.nav_procedures,
                R.id.nav_settings,
                R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)

        // check for updates
        RuStoreUpdate(this).checkForUpdates()

        // start advertising
        ruStoreAd.adView = MyTargetView(this)

        ruStoreAd.interstitialAd(context = applicationContext)

        // Request user's review
        RuStoreReview(this).rateApp()

        checkAndShowDialog()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> navController.navigate(R.id.nav_search)
            R.id.full_month_view -> navController.navigate(R.id.fullMonthViewFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        ruStoreAd.destroyAd()
        super.onDestroy()
    }

    private fun checkAndShowDialog() {
        // Получаем applicationId текущего приложения
        val currentApplicationId = this.packageName

        // Задаем applicationId, который нам нужно проверить
        val targetApplicationId = "com.example.projectnailsschedule"

        // Проверяем, совпадает ли applicationId текущего приложения с целевым applicationId
        if (currentApplicationId == targetApplicationId) {
            // Создаем диалоговое окно с текстом и кнопкой "OK"
            val dialogBuilder = AlertDialog.Builder(this)
            val message = "Уважаемый пользователь, " +
                    "данная версия приложения перестанет обновляться 08.10.2023. " +
                    "Чтобы продолжить получать обновления необходимо скачать новую версию " +
                    "(новая версия будет доступна в Rustore с 08.10.2023). " +
                    "Для переноса данных о записях, клиентах и т.д. в новую версию необходимо в " +
                    "текущей версии воспользоваться Экспортом данных, а в новой версии Импортом данных " +
                    "(данная функция доступна из Меню приложения). "

            val spannableMessage = SpannableString(message)
            spannableMessage.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, spannableMessage.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

            dialogBuilder.setMessage(spannableMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    // Обработчик нажатия на кнопку "OK"
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }


}