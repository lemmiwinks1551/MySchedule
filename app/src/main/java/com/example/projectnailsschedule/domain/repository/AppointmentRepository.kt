package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.AppointmentParams

interface AppointmentRepository {

    fun saveAppointment(appointmentParams: AppointmentParams): Boolean

    fun getAppointment(): AppointmentParams
}