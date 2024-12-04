package com.example.projectnailsschedule.domain.repository.api.calendarColorApi

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CalendarColorApi {
    @POST("/api/v1/calendar-color/post")
    suspend fun post(
        @Body calendarDateModelDb: CalendarDateModelDb,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/api/v1/calendar-color/delete")
    suspend fun delete(
        @Body calendarDateModelDb: CalendarDateModelDb,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/api/v1/calendar-color/get-last-timestamp")
    suspend fun getLastTimestamp(
        @Body user: UserInfoDto,
        @Header("Authorization") token: String
    ): Long

    @POST("/api/v1/calendar-color/get-remote-appointment-after-timestamp")
    suspend fun getDataAfterTimestamp(
        @Header("Authorization") token: String,
        @Body timestamp: Long?
    ): List<CalendarDateModelDb>

    @POST("/api/v1/calendar-color/get-count")
    suspend fun getCount(
        @Header("Authorization") token: String
    ): Long
}