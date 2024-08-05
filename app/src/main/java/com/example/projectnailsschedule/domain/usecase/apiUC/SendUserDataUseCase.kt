package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.domain.models.UserData
import com.example.projectnailsschedule.domain.repository.EventsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SendUserDataUseCase(var context: Context) {
    private val log = this::class.simpleName

    suspend fun execute(userData: UserData) {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://myschedule.myddns.me")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventsApi = retrofit.create(EventsApi::class.java)

        try {
            Log.i(log, "Отправляем данные $userData")
            eventsApi.postUserEvent(userData)
        } catch (e: Exception) {
            Log.i(log, "Не удалось отправить динные $userData")
            e.printStackTrace()
        }
    }
}