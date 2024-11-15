package com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC

import android.util.Log
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.data.storage.converters.DateTypeAdapter
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.repository.api.userDataApi.AppointmentsApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class GetUserRemoteAppointmentsAfterTimestampUseCase {
    private val log = this::class.simpleName
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
        .create()

    suspend fun execute(token: String, timestamp: Date?): List<AppointmentDto>? {
        val baseUrl = getBaseUrl()

        return try {
            val client = createOkHttpClient()
            val retrofit = createRetrofit(baseUrl, client)

            val appointmentsApi = retrofit.create(AppointmentsApi::class.java)

            val response = executeRequest(appointmentsApi, token, timestamp)

            return response
        } catch (e: Exception) {
            Log.e(log, e.message.toString())
            null
        }
    }

    private fun getBaseUrl(): String {
        return if (BuildConfig.DEBUG) {
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
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun executeRequest(
        appointmentsApi: AppointmentsApi,
        token: String,
        timestamp: Date?
    ): List<AppointmentDto> {
        return appointmentsApi.getRemoteAppointmentsAfterTimestamp(token, timestamp)
    }
}