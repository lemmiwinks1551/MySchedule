package com.example.projectnailsschedule.data.repository

import android.content.Context
import com.example.projectnailsschedule.data.storage.CalendarDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class CalendarRepositoryImpl(context: Context) : CalendarRepository {
    private var calendarDb = CalendarDb.getDb(context)

    override suspend fun insertDate(calendarDateModelDb: CalendarDateModelDb): Boolean {
        calendarDb.getDao().insert(calendarDateModelDb)
        return true
    }

    override suspend fun updateDate(calendarDateModelDb: CalendarDateModelDb): Boolean {
        calendarDb.getDao().update(calendarDateModelDb)
        return true
    }

    override suspend fun deleteDate(calendarDateModelDb: CalendarDateModelDb): Boolean {
        calendarDb.getDao().delete(calendarDateModelDb)
        return true
    }

    override suspend fun selectDate(date: String): CalendarDateModelDb {
        return calendarDb.getDao().selectDate(date)
    }
}