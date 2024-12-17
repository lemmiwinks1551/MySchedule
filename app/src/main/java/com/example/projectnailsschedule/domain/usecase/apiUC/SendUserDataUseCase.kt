package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.domain.models.UserData
import com.example.projectnailsschedule.domain.repository.api.EventsApi
import com.example.projectnailsschedule.util.Util
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
import javax.inject.Inject

class SendUserDataUseCase @Inject constructor(var context: Context) {
    /** UseCase по оправке данных о действия пользователя */
    private val log = this::class.simpleName

    suspend fun execute(userData: UserData) {
        return // не отправляем данные, отключено 17.12.2024

        val baseUrl = Util().getBaseUrl()

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        class CacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response: Response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.DAYS)
                    .build()
                return response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
        }

        class ForceCacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder: Request.Builder = chain.request().newBuilder()
                if (!isInternetAvailable(context)) {
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
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventsApi = retrofit.create(EventsApi::class.java)

        // Не отправляем ничего, если запущено из дебага
        if (!BuildConfig.DEBUG) {
            try {
                Log.i(log, "Отправляем данные $userData")
                val response = eventsApi.postUserEvent(userData)

                if (response.isSuccessful) {
                    Log.i(log, "Данные доставлены успешно $userData")
                } else {
                    Log.i(log, "Не удалось отправить данные $userData")
                }
            } catch (e: Exception) {
                Log.i(log, "Не удалось отправить данные $userData, ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024 // 10 mb
        val cacheDirectory = File(context.cacheDir, "user_data_cache")
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
}