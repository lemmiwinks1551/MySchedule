package com.example.projectnailsschedule.domain.repository.api.userDataApi

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

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
}