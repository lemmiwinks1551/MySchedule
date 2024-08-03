package com.example.projectnailsschedule.domain.usecase.apiUC

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.domain.models.UserEventManager
import com.example.projectnailsschedule.domain.repository.EventsApi
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SendUserDataUseCase(var context: Context) {

    suspend fun execute() {
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
            val userData = UserEventManager.getUserEvent()
            Log.i("AppLifecycleObserverSend", userData.event)
            eventsApi.postUserEvent(userData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}