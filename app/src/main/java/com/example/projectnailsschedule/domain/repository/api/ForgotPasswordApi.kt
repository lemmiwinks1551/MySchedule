package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.dto.RegistrationRequestDto
import com.example.projectnailsschedule.domain.models.dto.StatusResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPasswordApi {
    @POST("/api/v1/auth/forgot_password")
    suspend fun forgotPassword(
        @Body registrationRequestDto: RegistrationRequestDto
    ): Response<StatusResponseDto>
}