package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams

interface ScheduleRepository {

    fun saveAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    fun getDateAppointments(dateParams: DateParams) : Array<AppointmentModelDb>

    fun deleteAppointment(appointmentModelDb: AppointmentModelDb)
}