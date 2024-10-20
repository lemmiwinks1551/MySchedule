package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context
import android.widget.Toast
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.domain.models.User
import com.example.projectnailsschedule.domain.repository.api.LoginApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginUseCase(private val context: Context) {

    @Volatile
    private var isExecuting = false

    suspend fun execute(user: User): String? {
        val baseUrl: String

        if (BuildConfig.DEBUG) {
            baseUrl = "http://10.0.2.2:8080/"
            user.username = "kirill"
            user.password = "123123123"
        } else {
            baseUrl = "https://myschedule.myddns.me"
        }

        if (isExecuting) {
            // Если запрос уже выполняется, игнорируем новые нажатия
            return null
        }

        isExecuting = true
        return try {
            var jwt = ""
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

            val loginApi = retrofit.create(LoginApi::class.java)

            // Выполняем запрос
            val response = loginApi.loginUser(user)

            if (response.isSuccessful) {
                val loginResponse = response.body() // Извлекаем тело ответа
                if (loginResponse != null) {
                    jwt = loginResponse.token
                }
            } else {
                Toast.makeText(context, "Ошибка авторизации: ${response.code()}", Toast.LENGTH_LONG)
                    .show()
                throw Exception("Ошибка авторизации: ${response.code()}")
            }

            Toast.makeText(context, "Авторизация прошла успешно", Toast.LENGTH_LONG).show()

            // Возвращаем полученный токен
            jwt

        } finally {
            // Сброс состояния после завершения запроса
            isExecuting = false
        }
    }
}