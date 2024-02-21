package com.example.projectnailsschedule.domain.repository

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import java.time.LocalDate

interface ScheduleRepository {

    suspend fun insertAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    suspend fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    suspend fun deleteAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    suspend fun getDateAppointments(date: LocalDate): MutableList<AppointmentModelDb>

    suspend fun selectAllAppointmentsList(): List<AppointmentModelDb>

    suspend fun getMonthAppointments(dateMonth: String): MutableList<AppointmentModelDb>

    fun selectAllAppointmentsLiveData(): LiveData<List<AppointmentModelDb>>

    fun searchAppointment(searchQuery: String): LiveData<List<AppointmentModelDb>>
}