package com.example.projectnailsschedule.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.ActivityMainBinding
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.installStatus
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.my.target.ads.MyTargetView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val log = this::class.simpleName
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var connectedIcon: MenuItem
    private lateinit var disconnectedIcon: MenuItem
    private var disposable: Disposable? = null

    @Inject
    lateinit var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler
    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null
    private val ruStoreAd = RuStoreAd()
    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus == InstallStatus.DOWNLOADED) {
            Toast.makeText(
                applicationContext,
                R.string.update_app_toast,
                Toast.LENGTH_LONG
            ).show()
        }
        lifecycleScope.launch {
            delay(5.seconds)
            appUpdateManager.completeUpdate()
        }
    }

    private val isSyncing = AtomicBoolean(false)
    private val updatePeriodSec = 1L

    override fun onStart() {
        super.onStart()
        disposable = Observable.interval(0, updatePeriodSec, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (isSyncing.compareAndSet(false, true)) {
                    val threadName = Thread.currentThread().name
                    val startTime = System.currentTimeMillis()
                    val starTime = Date()
                    Log.i("Timer", "Синхронизация началась на потоке: $threadName в $startTime")

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            // Выполняем синхронизацию
                            mainViewModel.synchronizationCheckAppointments()
                            mainViewModel.synchronizationCheckCalendarDate()
                        } catch (e: Exception) {
                            Log.e("Sync", "Ошибка во время синхронизации: ${e.message}")
                        } finally {
                            // Освобождаем флаг
                            val endTime = Date()
                            val duration =
                                (endTime.time - starTime.time).toDuration(DurationUnit.MILLISECONDS)
                            Log.i(
                                "Timer",
                                "Синхронизация выполнена. Затрачено времени $duration"
                            )
                            isSyncing.set(false)
                        }
                    }
                } else {
                    Log.i("Sync", "Синхронизация уже выполняется, новая задача не запускается.")
                }
            }, { error ->
                Log.e("Sync", "Ошибка в Observable: ${error.message}")
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lifecycleObserver = AppLifecycleObserver()
        lifecycle.addObserver(lifecycleObserver)

        // set theme from shared prefs
        val currentUserTheme = mainViewModel.getUserTheme()
        val currentUserThemeId = resources.getIdentifier(currentUserTheme, "style", packageName)
        setTheme(currentUserThemeId)

        // set UserData singleton
        CoroutineScope(Dispatchers.IO).launch {
            mainViewModel.getUserInfoApi()
        }

        // Set uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // init all widgets
        initWidgets()

        // configure app bar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_calendar
                // в какие фрагменты устанавливать меню
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)

        // check for updates
        // RuStoreUpdate(this).checkForUpdates()
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForUpdates()

        // start advertising
        ruStoreAd.adView = MyTargetView(this)

        ruStoreAd.interstitialAd(context = applicationContext)

        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        this,
                        123
                    )
                }
            }
        }
    }

    private fun initWidgets() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        connectedIcon = menu.findItem(R.id.cloud_connected)!!
        disconnectedIcon = menu.findItem(R.id.cloud_disconnected)!!
        mainViewModel.menuStatus.postValue(true)

        // syncByClick()
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
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    private fun checkForUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }

            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    123
                )
            }
        }
    }

    private fun initObservers() {
        // Подключаем Observer к userDateQueue
        UserDataManager.userDateQueue.observe(this) { userDataQueue ->
            // Обрабатываем обновления userDateQueue
            CoroutineScope(Dispatchers.IO).launch {
                mainViewModel.sendUserData()
            }
        }

        mainViewModel.syncStatus.observe(this) {
            if (mainViewModel.menuStatus.value == true) {
                if (it) {
                    // показываем облачку ОК
                    connectedIcon.setVisible(true)
                    disconnectedIcon.setVisible(false)
                } else {
                    // показываем облачку офф
                    connectedIcon.setVisible(false)
                    disconnectedIcon.setVisible(true)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    private fun syncByClick() {
        disconnectedIcon.setOnMenuItemClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    mainViewModel.synchronizationCheckAppointments()
                    mainViewModel.synchronizationCheckCalendarDate()
                } catch (e: Exception) {
                    Log.e("Sync", e.message.toString())
                }
            }
            true
        }

        connectedIcon.setOnMenuItemClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    //mainViewModel.synchronizationCheck()
                    mainViewModel.synchronizationCheckCalendarDate()
                } catch (e: Exception) {
                    Log.e("Sync", e.message.toString())
                }
            }
            true
        }
    }
}