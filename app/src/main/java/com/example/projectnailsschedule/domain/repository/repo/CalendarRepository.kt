package com.example.projectnailsschedule.domain.repository.repo

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb

interface CalendarRepository {

    suspend fun insert(calendarDateModelDb: CalendarDateModelDb): Boolean

    suspend fun update(calendarDateModelDb: CalendarDateModelDb): Boolean

    suspend fun delete(calendarDateModelDb: CalendarDateModelDb): Boolean

    suspend fun select(date: String): CalendarDateModelDb

    suspend fun getAll(): List<CalendarDateModelDb>

    suspend fun getNotSync(): List<CalendarDateModelDb>

    suspend fun getDeleted(): List<CalendarDateModelDb>

    suspend fun getByLocalId(localAppointmentId: Long): CalendarDateModelDb?

    suspend fun getMaxTimestamp(): Long?

    suspend fun getBySyncUUID(uuid: String): CalendarDateModelDb?

    suspend fun getCount(): Long
}