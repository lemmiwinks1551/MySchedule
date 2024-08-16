package com.example.projectnailsschedule.domain.repository.api

import com.example.projectnailsschedule.domain.models.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EventsApi {
    @POST("/api/user-events")
    suspend fun postUserEvent(
        @Body userEvent: UserData
    ) : Response<Unit>
}
