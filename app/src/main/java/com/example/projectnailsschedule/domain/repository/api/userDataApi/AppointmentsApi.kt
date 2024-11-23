package com.example.projectnailsschedule.domain.repository.api.userDataApi

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.Date

interface AppointmentsApi {
    @POST("/api/v1/user-data/post-appointment")
    suspend fun postAppointment(
        @Body appointmentDto: AppointmentDto,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/api/v1/user-data/delete-appointment")
    suspend fun deleteAppointment(
        @Body appointmentDto: AppointmentDto,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("/api/v1/user-data/get-remote-appointments")
    suspend fun getUserRemoteAppointments(
        @Body user: UserInfoDto
    ): List<AppointmentDto>

    @POST("/api/v1/user-data/get-last-remote-appointment-timestamp")
    suspend fun getLastRemoteAppointmentTimestamp(
        @Body user: UserInfoDto,
        @Header("Authorization") token: String
    ): Date

    @POST("/api/v1/user-data/get-remote-appointment-after-timestamp")
    suspend fun getRemoteAppointmentsAfterTimestamp(
        @Header("Authorization") token: String,
        @Body timestamp: Date?
    ): List<AppointmentDto>

    @POST("/api/v1/user-data/get-count")
    suspend fun getCount(
        @Header("Authorization") token: String
    ): Long
}