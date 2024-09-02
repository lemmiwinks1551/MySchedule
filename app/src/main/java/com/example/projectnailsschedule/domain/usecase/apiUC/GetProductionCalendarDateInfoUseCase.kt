package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.ProductionCalendarDateModel
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.repository.api.ProductionCalendarApi
import kotlinx.coroutines.delay
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class GetProductionCalendarDateInfoUseCase(private val context: Context) {
    /** Данный UseCase работает только с кешем, чтобы избежать многократных запросов к серверу
     * данные с сервера за год получает другой UseCase*/

    private val log = this::class.simpleName

    suspend fun execute(selectedDate: DateParams, day: Int = 0): ProductionCalendarDateModel {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cacheDirectory = File(context.cacheDir, "prod_calendar_cache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())

        var attempt = 0
        val maxAttempts = 3
        val OFFLINE_MODE = true
        val delayBetweenAttempts = 10000L
        var getProductionCalendarDateInfoException: Exception? = null

        while (attempt < maxAttempts) {
            try {
                attempt++
                Log.i(log, "$log попытка получить данные из cache № $attempt")

                val client = OkHttpClient.Builder()
                    .cache(cache)  // Добавляем кэш в OkHttpClient
                    .addInterceptor(ETagInterceptor(cache, context, OFFLINE_MODE))
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://myschedule.myddns.me")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val productionCalendarApi = retrofit.create(ProductionCalendarApi::class.java)

                // Получаем обрабатываемый год, например "2024"
                val year = selectedDate.date?.year.toString()

                // вернуть информацию о конкретной дате
                return productionCalendarApi.getYearData(year)[day]
            } catch (e: Exception) {
                getProductionCalendarDateInfoException = e
                delay(delayBetweenAttempts)
            }
        }

        if (getProductionCalendarDateInfoException != null) {
            // TODO: Если данные в кеше не находит спамит сильно ошибками на сервер (30 раз за каждый месяц)
            //  пока выключил
            // отправить данные об ошибке
            //UserDataManager.updateUserData(event = getProductionCalendarDateInfoException.message)
        }

        // Если данные не найдены - возвращает рабочий день-шаблон
        return ProductionCalendarDateModel(0, "", 1, "", "", "")
    }
}