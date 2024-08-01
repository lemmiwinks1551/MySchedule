package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.projectnailsschedule.domain.models.UserEventManager
import com.example.projectnailsschedule.domain.repository.EventsApi
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class SendUserDataUseCase(var context: Context) {
    // здесь реализация отправки данных на сервер

    suspend fun execute() {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        class CacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response: Response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(10, TimeUnit.DAYS)
                    .build()
                return response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
        }

        class ForceCacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder: Request.Builder = chain.request().newBuilder()
                if (!isInternetAvailable(context)) { // Функция для проверки доступности интернета
                    builder.cacheControl(CacheControl.FORCE_CACHE)
                }
                return chain.proceed(builder.build())
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(CacheInterceptor())
            .addInterceptor(ForceCacheInterceptor())
            .cache(createOkHttpClient(context).cache)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://myschedule.myddns.me")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventsApi = retrofit.create(EventsApi::class.java)

        // Отправка данных на сервер
        try {
            eventsApi.postUserEvent(UserEventManager.getUserEvent())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
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

    private fun createOkHttpClient(context: Context): OkHttpClient {
        // Размер кэша - 100 МБ
        val cacheSize = 100 * 1024 * 1024
        val cacheDirectory = File(context.cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())

        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }
}