package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context
import android.widget.Toast
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.domain.repository.api.LogoutApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LogoutUseCase(private val context: Context) {

    @Volatile
    private var isExecuting = false

    suspend fun execute(jwt: String): Boolean? {
        val baseUrl: String

        if (BuildConfig.DEBUG) {
            baseUrl = "http://10.0.2.2:8080/"
        } else {
            baseUrl = "https://myschedule.myddns.me"
        }

        if (isExecuting) {
            // Если запрос уже выполняется, игнорируем новые нажатия
            return null
        }

        isExecuting = true
        return try {
            // Выполнение запроса
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val logoutApi = retrofit.create(LogoutApi::class.java)

            // Выполняем запрос
            val response = logoutApi.logout(jwt)

            if (response.isSuccessful) {
                Toast.makeText(context, "Вы вышли из аккаунта", Toast.LENGTH_LONG).show()
                true
            } else {
                Toast.makeText(context, "Ошибка: ${response.code()}", Toast.LENGTH_LONG).show()
                false
            }
        } finally {
            // Сброс состояния после завершения запроса
            isExecuting = false
        }
    }
}
