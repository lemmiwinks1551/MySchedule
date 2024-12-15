package com.example.projectnailsschedule.domain.repository.api

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutApi {
    @POST("/api/v1/auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>
}
