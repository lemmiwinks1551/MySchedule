package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.ProductionCalendarDateModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductionCalendarApi {
    @GET("/api/calendar/get-year/{year}")
    suspend fun getYearData(
        @Path("year") year: String
    ): List<ProductionCalendarDateModel>
}