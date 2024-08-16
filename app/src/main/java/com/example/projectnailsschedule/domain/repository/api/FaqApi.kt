package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.FaqModel
import retrofit2.http.Body
import retrofit2.http.GET

interface FaqApi {
    @GET("/faq")
    suspend fun getFaq(): List<FaqModel>
}