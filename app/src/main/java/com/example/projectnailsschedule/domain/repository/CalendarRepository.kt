package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb

interface CalendarRepository {
    suspend fun insertDate(calendarDateModelDb: CalendarDateModelDb): Boolean

    suspend fun updateDate(calendarDateModelDb: CalendarDateModelDb): Boolean

    suspend fun deleteDate(calendarDateModelDb: CalendarDateModelDb): Boolean

    suspend fun selectDate(date: String): CalendarDateModelDb
}