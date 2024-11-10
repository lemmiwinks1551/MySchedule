package com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC

import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.data.storage.converters.DateTypeAdapter
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.repository.api.userDataApi.AppointmentsApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class PostAppointmentUseCase {
    private val log = this::class.simpleName
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
        .create()

    suspend fun execute(appointmentDto: AppointmentDto, jwt: String): String {
        val baseUrl = getBaseUrl()


        return try {
            val client = createOkHttpClient()
            val retrofit = createRetrofit(baseUrl, client)

            val appointmentsApi = retrofit.create(AppointmentsApi::class.java)

            // Выполняем запрос на авторизацию
            val response = executeRequest(appointmentsApi, appointmentDto, jwt)
            delay(100L)
            return response.code().toString()
        } catch (e: Exception) {
            "Возникла непредвиденная ошибка"
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
        appointmentDto: AppointmentDto,
        jwt: String
    ): Response<Unit> {
        return appointmentsApi.postAppointment(appointmentDto, jwt)
    }
}