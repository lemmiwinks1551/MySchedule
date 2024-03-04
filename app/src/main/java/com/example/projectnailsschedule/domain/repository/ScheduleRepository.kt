package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import java.time.LocalDate

interface ScheduleRepository {

    suspend fun insertAppointment(appointmentModelDb: AppointmentModelDb): Long

    suspend fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    suspend fun deleteAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    suspend fun searchAppointment(searchQuery: String): MutableList<AppointmentModelDb>

    suspend fun getDateAppointments(date: LocalDate): MutableList<AppointmentModelDb>
}