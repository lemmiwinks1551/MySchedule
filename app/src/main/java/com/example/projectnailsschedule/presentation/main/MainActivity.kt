package com.example.projectnailsschedule.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.RelativeLayout
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
import com.google.android.material.navigation.NavigationView
import com.my.target.ads.InterstitialAd
import com.my.target.ads.InterstitialAd.InterstitialAdListener
import com.my.target.ads.MyTargetView
import com.my.target.ads.MyTargetView.MyTargetViewListener
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateInfo
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.core.tasks.OnCompleteListener
import ru.vk.store.sdk.review.RuStoreReviewManagerFactory
import ru.vk.store.sdk.review.model.ReviewInfo

class MainActivity : AppCompatActivity() {
    private val log = this::class.simpleName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val uncaughtExceptionHandler = UncaughtExceptionHandler()
    private var mainViewModel: MainViewModel? = null

    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    // Banner
    private var adView: MyTargetView? = null
    private var adViewLayoutParams: RelativeLayout.LayoutParams? = null

    // InterstitialAd
    private var ad: InterstitialAd? = null

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
        checkForUpdates()

        // rate app
        // rateApp()
    }

    private fun banner() {
        val slotId = 1284152
        // Включение режима отладки
        // MyTargetManager.setDebugMode(true)

        // Создаем экземпляр MyTargetView
        adView = MyTargetView(this)

        // Задаём id слота
        adView!!.setSlotId(slotId)

        // Устанавливаем LayoutParams
        adViewLayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        adView!!.layoutParams = adViewLayoutParams

        // Устанавливаем слушатель событий
        adView!!.listener = object : MyTargetViewListener {
            override fun onLoad(myTargetView: MyTargetView) {
                // Данные успешно загружены, запускаем показ объявлений
                Log.e(log, "onLoad")
                binding.root.addView(adView)
            }
            override fun onNoAd(reason: String, myTargetView: MyTargetView) {
                Log.e(log, "onNoAd")
            }
            override fun onShow(myTargetView: MyTargetView) {
                Log.e(log, "onShow")
            }
            override fun onClick(myTargetView: MyTargetView) {
                Log.e(log, "onClick")
            }
        }

        // Запускаем загрузку данных
        adView!!.load()
    }

    private fun interstitialAd() {
        val slotId = 1285135

        // Создаем экземпляр InterstitialAd
        ad = InterstitialAd(slotId, this)

        // Устанавливаем слушатель событий
        ad!!.setListener(object : InterstitialAdListener {
            override fun onLoad(ad: InterstitialAd) {
                // Запускаем показ в отдельном Activity
                ad.show()
            }
            override fun onNoAd(reason: String, ad: InterstitialAd) {
                Log.e(log, "onNoAd")
            }
            override fun onClick(ad: InterstitialAd) {}
            override fun onDisplay(ad: InterstitialAd) {}
            override fun onDismiss(ad: InterstitialAd) {}
            override fun onVideoCompleted(ad: InterstitialAd) {}
        })

        // Запускаем загрузку данных
        ad!!.load()
    }

    private fun rateApp() {
        // Для работы с оценками необходимо создать RuStoreReviewManager с помощью RuStoreReviewManagerFactory:
        val manager = RuStoreReviewManagerFactory.create(this)

        // Вызовите requestReviewFlow() заранее перед вызовом launchReviewFlow(reviewInfo),
        // чтобы подготовить необходимую информацию для отображения экрана.
        // ReviewInfo имеет свой срок жизни — около пяти минут.
        manager.requestReviewFlow().addOnCompleteListener(object : OnCompleteListener<ReviewInfo> {
            override fun onFailure(throwable: Throwable) {
                // Handle error
                // Если получен ответ onFailure, то не рекомендуем самостоятельно отображать ошибку пользователю,
                // так как пользователь не запускал данный процесс.
            }

            override fun onSuccess(result: ReviewInfo) {
                // Save reviewInfo
                // Если получен ответ onSuccess, то необходимо локально сохранить ReviewInfo,
                // для последующего вызова launchReviewFlow(reviewInfo).
            }
        })

        // Для запуска формы запроса оценки и отзыва о приложении у пользователя вызовите метод launchReviewFlow(reviewInfo),
        // используя ранее полученный ReviewInfo.
    }

    private fun checkForUpdates() {
        val updateManager = RuStoreAppUpdateManagerFactory.create(context = this)

        var appUpdateInfo: AppUpdateInfo? = null

        updateManager
            .getAppUpdateInfo()
            .addOnSuccessListener { info ->
                appUpdateInfo = info
                Log.e("checkUpdate", appUpdateInfo!!.updateAvailability.toString())
                updateManager
                    .startUpdateFlow(appUpdateInfo!!, AppUpdateOptions.Builder().build())
                    .addOnSuccessListener { resultCode ->
                        Log.e("checkUpdate", resultCode.toString())
                    }
                    .addOnFailureListener { throwable ->
                        Log.e("checkUpdate", throwable.toString())
                    }
            }
            .addOnFailureListener { throwable ->
                Log.e("checkUpdate", throwable.message!!)
            }

        updateManager.registerListener { state ->
            if (state.installStatus == InstallStatus.DOWNLOADED) {
                // Update is ready to install
                Log.e("checkUpdate", "Update is ready to install")
                updateManager
                    .completeUpdate()
                    .addOnFailureListener { throwable ->
                        Log.e("checkUpdate", throwable.message!!)
                    }
            }
        }
    }

    private fun initWidgets() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)

        // show banner
        // banner()

        // show banner
        interstitialAd()
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

    override fun onDestroy() {
        super.onDestroy()
        if (adView != null) {
            adView!!.destroy()
            super.onDestroy()
        }
    }
}