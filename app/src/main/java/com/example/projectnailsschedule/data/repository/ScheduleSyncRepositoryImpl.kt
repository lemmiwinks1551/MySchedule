package com.example.projectnailsschedule.data.repository

import android.content.Context
import com.example.projectnailsschedule.data.storage.ScheduleSyncDb
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.repository.repo.ScheduleSyncRepository
import java.util.Date

class ScheduleSyncRepositoryImpl(context: Context) : ScheduleSyncRepository {
    private var dao = ScheduleSyncDb.getDb(context).getDao()

    override suspend fun insert(appointmentDto: AppointmentDto): Long {
        return dao.insert(appointmentDto)
    }

    override suspend fun update(appointmentDto: AppointmentDto) {
        dao.update(appointmentDto)
    }

    override suspend fun delete(appointmentDto: AppointmentDto) {
        dao.delete(appointmentDto)
    }

    override suspend fun getAll(): List<AppointmentDto> {
        return dao.getAll()
    }

    override suspend fun getNotSyncAppointments(): List<AppointmentDto> {
        return dao.getNotSyncAppointments()
    }

    override suspend fun getDeletedAppointments(): List<AppointmentDto> {
        return dao.getDeletedAppointments()
    }

    override suspend fun getByLocalAppointmentId(localAppointmentId: Long): AppointmentDto? {
        return dao.getByLocalAppointmentId(localAppointmentId)
    }

    override suspend fun getMaxAppointmentTimestamp(): Date? {
        return dao.getMaxAppointmentTimestamp()
    }
}