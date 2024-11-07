package com.example.projectnailsschedule.domain.repository.api.userDataApi

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
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

    //
}