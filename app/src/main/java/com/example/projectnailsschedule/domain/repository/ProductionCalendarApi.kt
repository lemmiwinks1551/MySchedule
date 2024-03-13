package com.example.projectnailsschedule.domain.repository


import com.example.projectnailsschedule.domain.models.ResponseData
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductionCalendarApi {
    @GET("get/ru/{date}/json")
    suspend fun getDateStatus(@Path("date") date: String): ResponseData
}