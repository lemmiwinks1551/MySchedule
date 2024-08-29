package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException

class ETagInterceptor(private val cache: Cache, private val context: Context) : Interceptor {
    private val log = this::class.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheKey = request.url.toString()
        val cacheFile = File(cache.directory, cacheKey.hashCode().toString())

        // Если интернета нет, пытаемся получить данные из кэша
        if (!internetAvailable(context)) {
            if (cacheFile.exists()) {
                Log.d(log, "Интернет недоступен, данные загружаются из Cache")
                val cachedETag = cacheFile.readText().substringBefore("\n")
                val cachedData = cacheFile.readText().substringAfter("\n")

                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200) // Успешный ответ
                    .message("OK")
                    .body(ResponseBody.create(null, cachedData))
                    .build()
            } else {
                throw IOException("Интернет недоступен и данных в кэше нет")
            }
        }
        // Выполнение запроса
        val response = chain.proceed(request)

        // Получение ETag из заголовков ответа
        val eTag = response.header("ETag")

        if (eTag != null) {
            // Попытка загрузить данные из файлового кэша
            Log.d(log, "В response.header.ETag найден")
            val cacheFile = File(cache.directory, cacheKey.hashCode().toString())
            if (cacheFile.exists()) {
                Log.d(log, "ETag найден в Cache")
                val cachedETag = cacheFile.readText().substringBefore("\n")
                val cachedData = cacheFile.readText().substringAfter("\n")

                if (cachedETag == eTag) {
                    Log.d(log, "Данные из Cache загружены")
                    // Если ETag совпадает, используем данные из кэша
                    return response.newBuilder()
                        .body(ResponseBody.create(response.body?.contentType(), cachedData))
                        .build()
                }
            }

            // Если ETag изменился или данных нет в кэше, обновляем кэш
            response.body?.let { body ->
                Log.d(log, "ETag не найден в Cache или изменился")
                val responseBodyString = body.string()

                // Сохранение ETag и данных в кэш
                cacheFile.writeText("$eTag\n$responseBodyString")
                Log.d(log, "Новый ETag сохранен в Cache")

                // Возвращаем новый ответ с телом
                return response.newBuilder()
                    .body(ResponseBody.create(body.contentType(), responseBodyString))
                    .build()
            }
        }

        return response
    }

    private fun internetAvailable(context: Context): Boolean {
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
