package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.domain.models.FaqModel
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.repository.api.FaqApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class GetFaqUseCase(private val context: Context) {
    private val log = this::class.simpleName

    suspend fun execute(): List<FaqModel> {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cacheDirectory = File(context.cacheDir, "faq_cache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())

        do {
            try {
                val client = OkHttpClient.Builder()
                    .cache(cache)  // Добавляем кэш в OkHttpClient
                    .addInterceptor(ETagInterceptor(cache, context))
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://myschedule.myddns.me")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val faqApi = retrofit.create(FaqApi::class.java)

                // Выполняем запрос
                val faq = faqApi.getFaq()

                return faq
            } catch (e: Exception) {
                e.message?.let { Log.e(log, it) }
                UserDataManager.updateUserData(event = e.message)
                continue
            }
        } while (true)
    }
}
