package com.example.projectnailsschedule.domain.repository.api.userDataApi

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AppointmentsApi {
    @POST("/api/v1/user-data/post-appointment")
    suspend fun postAppointment(
        @Body appointment: AppointmentModelDb,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/api/v1/user-data/delete-appointment")
    suspend fun deleteAppointment(
        @Body appointment: AppointmentModelDb,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/api/v1/user-data/get-remote-appointments")
    suspend fun getUserRemoteAppointments(
        @Body user: UserInfoDto
    ): List<AppointmentModelDb>

    @POST("/api/v1/user-data/get-last-remote-appointment-timestamp")
    suspend fun getLastRemoteAppointmentTimestamp(
        @Body user: UserInfoDto,
        @Header("Authorization") token: String
    ): Long

    @POST("/api/v1/user-data/get-remote-appointment-after-timestamp")
    suspend fun getRemoteAppointmentsAfterTimestamp(
        @Header("Authorization") token: String,
        @Body timestamp: Long?
    ): List<AppointmentModelDb>

    @POST("/api/v1/user-data/get-count")
    suspend fun getCount(
        @Header("Authorization") token: String
    ): Long

    @POST("/api/v1/user-data/enable-sync")
    suspend fun enableSync(
        @Body user: UserInfoDto,
        @Header("Authorization") token: String
    ): Boolean

    @POST("/api/v1/user-data/disable-sync")
    suspend fun disableSync(
        @Body user: UserInfoDto,
        @Header("Authorization") token: String
    ): Boolean
}