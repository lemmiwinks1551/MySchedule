package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.dto.RegistrationRequestDto
import com.example.projectnailsschedule.domain.models.dto.RegistrationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {
    @POST("/api/v1/registration")
    suspend fun registerUser(
        @Body registrationRequestDto: RegistrationRequestDto
    ): Response<RegistrationResponseDto>
}