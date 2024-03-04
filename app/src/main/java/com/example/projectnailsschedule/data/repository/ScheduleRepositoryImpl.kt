package com.example.projectnailsschedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.util.Util
import com.google.android.exoplayer2.util.Log
import java.time.LocalDate

class ScheduleRepositoryImpl(context: Context) : ScheduleRepository {
    private var dao = ScheduleDb.getDb(context).getDao()

    override suspend fun insertAppointment(appointmentModelDb: AppointmentModelDb): Long {
        return dao.insert(appointmentModelDb = appointmentModelDb)
    }

    override suspend fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        dao.update(appointmentModelDb = appointmentModelDb)
        return true
    }

    override suspend fun deleteAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        dao.delete(appointmentModelDb = appointmentModelDb)
        return true
    }

    override suspend fun getDateAppointments(date: LocalDate): MutableList<AppointmentModelDb> {
        return dao.getDateAppointments(Util().dateConverterNew(date.toString()))
    }

    override suspend fun searchAppointment(searchQuery: String): MutableList<AppointmentModelDb> {
        return dao.searchAppointment(searchQuery = searchQuery)
    }

}