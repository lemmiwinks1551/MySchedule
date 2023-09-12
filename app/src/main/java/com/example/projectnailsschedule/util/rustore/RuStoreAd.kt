package com.example.projectnailsschedule.util.rustore

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.my.target.ads.InterstitialAd
import com.my.target.ads.MyTargetView
import com.my.target.common.MyTargetManager

class RuStoreAd {

    // Banner
    private lateinit var adView: MyTargetView

    // InterstitialAd
    private lateinit var interstitialAd: InterstitialAd

    fun interstitialAd(context: Context) {
        val log = "InterstitialAd"

        val slotId = 1285135

        // Создаем экземпляр InterstitialAd
        interstitialAd = InterstitialAd(slotId, context)

        // Устанавливаем слушатель событий
        interstitialAd.setListener(object : InterstitialAd.InterstitialAdListener {
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
        interstitialAd.load()
    }

    fun banner(
        context: Context,
        layout: ConstraintLayout
    ) {
        val log = "BannerAd"

        val slotId = 1404141

        // Включение режима отладки
        // MyTargetManager.setDebugMode(true)

        /*        GlobalScope.launch {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
            Log.d("text", adInfo.id ?: "unknown")
        }*/

        // Создаем экземпляр MyTargetView
        adView = MyTargetView(context)

        // Задаём id слота
        adView.setSlotId(slotId)

        val adViewLayoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        adViewLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        adViewLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        adViewLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        adView.layoutParams = adViewLayoutParams

        adView.listener = object : MyTargetView.MyTargetViewListener {
            override fun onLoad(myTargetView: MyTargetView) {
                // Данные успешно загружены, запускаем показ объявлений
                Log.e(log, "onLoad")
                layout.addView(adView)
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
        adView.load()
    }

    fun destroyAd() {
        adView.destroy()
        interstitialAd.destroy()
    }
}