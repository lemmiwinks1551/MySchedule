package com.example.projectnailsschedule.util.rustore

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.projectnailsschedule.databinding.ActivityMainBinding
import com.my.target.ads.InterstitialAd
import com.my.target.ads.MyTargetView

class RuStoreAd(val context: Context, val binding: ActivityMainBinding) {
    val log = "RuStoreAd"

    // Banner
    private var adView: MyTargetView? = null
    private var adViewLayoutParams: RelativeLayout.LayoutParams? = null

    // InterstitialAd
    private var interstitialAd: InterstitialAd? = null

    fun interstitialAd() {
        val slotId = 1285135

        // Создаем экземпляр InterstitialAd
        interstitialAd = InterstitialAd(slotId, context)

        // Устанавливаем слушатель событий
        interstitialAd!!.setListener(object : InterstitialAd.InterstitialAdListener {
            override fun onLoad(ad: InterstitialAd) {
                // Запускаем показ в отдельном Activity
                Log.e(log, "onLoad")
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
        interstitialAd!!.load()
    }

    fun banner() {
        val slotId = 1284152
        // Включение режима отладки
        // MyTargetManager.setDebugMode(true)

        // Создаем экземпляр MyTargetView
        adView = MyTargetView(context)

        // Задаём id слота
        adView!!.setSlotId(slotId)

        // Устанавливаем LayoutParams
        adViewLayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        adView!!.layoutParams = adViewLayoutParams

        // Устанавливаем слушатель событий
        adView!!.listener = object : MyTargetView.MyTargetViewListener {
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

    fun destroyAd() {
        if (adView != null) {
            adView!!.destroy()
        }
    }
}