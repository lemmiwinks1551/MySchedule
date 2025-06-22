package com.example.projectnailsschedule.domain.repository.api.premiumApi

import com.example.projectnailsschedule.domain.models.dto.PremiumStatusDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PremiumApi {
    @POST("/api/user/premium-status")
    suspend fun postPremiumStatus(
        @Body premiumStatusDto: PremiumStatusDto,
        @Header("Authorization") token: String
    ): Response<Unit>
}

