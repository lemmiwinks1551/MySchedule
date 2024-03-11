package com.example.projectnailsschedule.domain.repository

import okhttp3.ResponseBody
import retrofit2.http.GET

interface isDayOffApi {
    @GET("api/getdata?year=2024&month=05&day=09")
    suspend fun getDayStatus(): ResponseBody
}