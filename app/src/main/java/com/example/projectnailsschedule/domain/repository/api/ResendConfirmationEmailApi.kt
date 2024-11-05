package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.dto.StatusResponseDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ResendConfirmationEmailApi {
    @POST("/api/v1/auth/resend-confirmation-email")
    suspend fun resendConfirmationEmailApi(
        @Body userInfoDto: UserInfoDto
    ): Response<StatusResponseDto>
}