package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UsernameRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GetUserDataApi {
    @POST("/api/v1/auth/get_user_data")
    suspend fun getUserData(
        @Body usernameRequest: UsernameRequestDto,
        @Header("Authorization") token: String
    ): Response<UserInfoDto>
}
