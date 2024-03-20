package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.ResponseData
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductionCalendarApi {
    @GET("/get/ru/{year}/json")
    suspend fun getYearData(
        @Path("year") year: String
    ): ResponseData
}