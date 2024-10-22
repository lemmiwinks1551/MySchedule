package com.example.projectnailsschedule.domain.usecase.account

import android.util.Log
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.domain.models.LoginResponse
import com.example.projectnailsschedule.domain.models.User
import com.example.projectnailsschedule.domain.repository.api.LoginApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginUseCase {
    private val log = this::class.simpleName

    suspend fun execute(user: User): String? {
        val baseUrl = getBaseUrl(user)

        return try {
            val client = createOkHttpClient()
            val retrofit = createRetrofit(baseUrl, client)

            val loginApi = retrofit.create(LoginApi::class.java)

            // Выполняем запрос на авторизацию
            val response = executeLoginRequest(loginApi, user)

            // Проверяем успешность ответа и возвращаем токен
            if (response.isSuccessful) {
                return response.body()?.token
            }
            null
        } catch (e: Exception) {
            Log.e(log, e.toString())
            null
        }
    }

    private fun getBaseUrl(user: User): String {
        return if (BuildConfig.DEBUG) {
            user.username = "kirill"
            user.password = "123123123"
            "http://10.0.2.2:8080/"
        } else {
            "https://myschedule.myddns.me"
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private fun createRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun executeLoginRequest(loginApi: LoginApi, user: User): Response<LoginResponse> {
        return loginApi.loginUser(user)
    }

}