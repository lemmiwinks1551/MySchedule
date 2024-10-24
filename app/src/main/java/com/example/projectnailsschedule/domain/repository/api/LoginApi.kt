package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.dto.LoginResponseDto
import com.example.projectnailsschedule.domain.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/v1/auth/login")
    suspend fun loginUser(
        @Body user: User
    ): Response<LoginResponseDto>
}