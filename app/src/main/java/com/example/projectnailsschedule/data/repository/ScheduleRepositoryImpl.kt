package com.example.projectnailsschedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.util.Util
import java.time.LocalDate

class ScheduleRepositoryImpl(context: Context) : ScheduleRepository {
    private var dao = ScheduleDb.getDb(context).getDao()

    override suspend fun insertAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        dao.insert(appointmentModelDb = appointmentModelDb)
        return true
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

    override suspend fun selectAllAppointmentsList(): List<AppointmentModelDb> {
        return dao.selectAllAppointmentsList()
    }

    override suspend fun getMonthAppointments(dateMonth: String): MutableList<AppointmentModelDb> {
        return dao.getMonthAppointments(dateMonth = dateMonth)
    }

    override fun selectAllAppointmentsLiveData(): LiveData<List<AppointmentModelDb>> {
        return dao.selectAllAppointmentsLiveData().asLiveData()
    }

    override fun searchAppointment(searchQuery: String): LiveData<List<AppointmentModelDb>> {
        return dao.searchAppointment(searchQuery = searchQuery).asLiveData()
    }

}