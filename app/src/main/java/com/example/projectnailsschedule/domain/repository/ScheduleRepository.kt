package com.example.projectnailsschedule.domain.repository

import android.database.Cursor
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams

interface ScheduleRepository {

    fun saveAppointment(appointmentParams: AppointmentParams): Boolean

    fun editAppointment(appointmentParams: AppointmentParams): Boolean

    fun getDateAppointments(dateParams: DateParams) : Cursor

    fun deleteAppointment(id: Int)

    fun getAllAppointments() : Cursor
}