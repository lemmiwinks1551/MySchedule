package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.repository.api.ProductionCalendarApi
import kotlinx.coroutines.delay
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class GetProductionCalendarYearUseCase(private val context: Context) {
    /** Данный UseCase получает данные о производственном календаре за выбранный год с сервера*/
    private val log = this::class.simpleName

    suspend fun execute(year: String) {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cacheDirectory = File(context.cacheDir, "prod_calendar_cache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())

        var attempt = 0
        val maxAttempts = 3
        val delayBetweenAttempts = 30000L
        var getProductionCalendarYearException: Exception? = null

        while (attempt < maxAttempts) {
            try {
                attempt++
                Log.i(log, "$log Получаем данные от сервера за год $year. Попытка № $attempt")

                val client = OkHttpClient.Builder()
                    .cache(cache)  // Добавляем кэш в OkHttpClient
                    .addInterceptor(ETagInterceptor(cache, context))
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://myschedule.myddns.me")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val productionCalendarApi = retrofit.create(ProductionCalendarApi::class.java)

                // Считать с сервера информацию о производственном календаре за год
                productionCalendarApi.getYearData(year)

                // Если ошибок не возникло - выходим из цикла
                break
            } catch (e: Exception) {
                getProductionCalendarYearException = e
                delay(delayBetweenAttempts)
            }
        }

        if (getProductionCalendarYearException != null) {
            // отправить данные об ошибке
            UserDataManager.updateUserData(event = getProductionCalendarYearException.message)
        }
    }
}