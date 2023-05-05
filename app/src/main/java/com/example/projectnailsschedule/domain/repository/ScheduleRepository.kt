package com.example.projectnailsschedule.domain.repository

import android.database.Cursor
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams

interface ScheduleRepository {

    fun saveAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean

    fun getDateAppointments(dateParams: DateParams) : Array<AppointmentModelDb>

    fun deleteAppointment(id: Int)

    fun getAllAppointments() : Cursor
}