package com.example.projectnailsschedule.data.repository

import android.content.Context
import com.example.projectnailsschedule.data.storage.CalendarDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class CalendarRepositoryImpl(context: Context) : CalendarRepository {
    private var dao = CalendarDb.getDb(context).getDao()

    override suspend fun insert(calendarDateModelDb: CalendarDateModelDb): Boolean {
        dao.insert(calendarDateModelDb)
        return true
    }

    override suspend fun update(calendarDateModelDb: CalendarDateModelDb): Boolean {
        dao.update(calendarDateModelDb)
        return true
    }

    override suspend fun delete(calendarDateModelDb: CalendarDateModelDb): Boolean {
        dao.delete(calendarDateModelDb)
        return true
    }

    override suspend fun select(date: String): CalendarDateModelDb {
        return dao.selectDate(date)
    }

    override suspend fun getAll(): List<CalendarDateModelDb> {
        return dao.getAll()
    }

    override suspend fun getNotSync(): List<CalendarDateModelDb> {
        return dao.getNotSync()
    }

    override suspend fun getDeleted(): List<CalendarDateModelDb> {
        return dao.getDeleted()
    }

    override suspend fun getByLocalId(id: Long): CalendarDateModelDb? {
        return dao.getById(id)
    }

    override suspend fun getMaxTimestamp(): Long? {
        return dao.getMaxTimestamp()
    }

    override suspend fun getBySyncUUID(uuid: String): CalendarDateModelDb? {
        return dao.getBySyncUUID(uuid)
    }

    override suspend fun getCount(): Long {
        return dao.getCount()
    }
}