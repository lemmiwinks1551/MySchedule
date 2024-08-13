package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.ProductionCalendarDateModel
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.repository.ProductionCalendarApi
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class GetProductionCalendarDateInfoUseCase(private val context: Context) {
    private val log = this::class.simpleName

    suspend fun execute(selectedDate: DateParams, day: Int = 0): ProductionCalendarDateModel {
        do {
            try {
                val interceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

                class CacheInterceptor : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request = chain.request()
                        val response: Response = chain.proceed(request)
                        val cacheControl = CacheControl.Builder()
                            .maxAge(10, TimeUnit.DAYS)
                            .build()
                        return response.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .build()
                    }
                }

                class ForceCacheInterceptor : Interceptor {
                    /*Позволяет использовать данные из кэша, если интернет недоступен,
                    и устанавливает правила для использования кэша даже при наличии интернета.*/
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request = chain.request()
                        val cacheControl = if (!isInternetAvailable(context)) {
                            CacheControl.FORCE_CACHE
                        } else {
                            CacheControl.Builder()
                                .maxStale(
                                    10,
                                    TimeUnit.DAYS
                                ) // Возвращать данные из кэша, даже если они устарели, но не 10 дней
                                .build()
                        }
                        val newRequest = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build()
                        return chain.proceed(newRequest)
                    }
                }

                class CacheLoggingInterceptor : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request = chain.request()
                        val response = chain.proceed(request)

                        val cacheControl = response.cacheControl.toString()
                        val responseBody =
                            response.body?.string() // Получаем тело ответа как строку для логирования

                        Log.d("CacheLoggingInterceptor", "Response Message: ${response.message}")
                        Log.d(
                            "CacheLoggingInterceptor",
                            "Response isSuccessful: ${response.isSuccessful}"
                        )
                        Log.d("CacheLoggingInterceptor", "Response code: ${response.code}")
                        Log.d("CacheLoggingInterceptor", "Cache-Control: $cacheControl")
                        Log.d("CacheLoggingInterceptor", "Response Body: $responseBody")

                        if (response.cacheResponse == null) {
                            Log.d("CacheLoggingInterceptor", "Response from server")
                        } else {
                            Log.d("CacheLoggingInterceptor", "Response from cache")
                        }

                        return response.newBuilder()
                            .body(responseBody?.toResponseBody(response.body?.contentType()))
                            .build()
                    }
                }

                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(CacheInterceptor())
                    .addInterceptor(ForceCacheInterceptor())
                    .cache(createOkHttpClient(context).cache)
                    .addInterceptor(CacheLoggingInterceptor())
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://myschedule.myddns.me")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val productionCalendarApi = retrofit.create(ProductionCalendarApi::class.java)

                val year =
                    selectedDate.date?.year.toString() // Получаем обрабатываемый год, например "2024"

                // вернуть информацию о конкретной дате
                return productionCalendarApi.getYearData(year)[day]
            } catch (e: Exception) {
                e.message?.let { Log.e(log, it) }
                // отправить данные об ошибке
                UserDataManager.updateUserData(event =  e.message)
                continue

                // вернуть простой рабочий день
                /*                return ProductionCalendarDateModel(
                                    id = 0,
                                    date = selectedDate.date.toString(),
                                    typeId = 1,
                                    typeText = "",
                                    note = "",
                                    weekDay = ""
                                )*/
            }
        } while (true)
    }
}

private fun createOkHttpClient(context: Context): OkHttpClient {
    val cacheSize = 10 * 1024 * 1024 // 10 mb
    val cacheDirectory = File(context.cacheDir, "prod_calendar_cache")
    val cache = Cache(cacheDirectory, cacheSize.toLong())

    return OkHttpClient.Builder()
        .cache(cache)
        .build()
}

private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected ?: false
    }
}
