package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.UserEventSingleton
import retrofit2.http.Body
import retrofit2.http.POST

interface EventsApi {
    @POST("/api/user-events")
    suspend fun postUserEvent(
        @Body userEvent: UserEventSingleton
    )
}
