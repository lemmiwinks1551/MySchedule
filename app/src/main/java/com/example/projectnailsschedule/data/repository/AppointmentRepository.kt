package com.example.projectnailsschedule.data.repository

import com.example.projectnailsschedule.domain.models.AppointmentModel

class AppointmentRepository {

    fun saveAppointment(appointmentModel: AppointmentModel): Boolean {
        /** Save appointment in database */
        return true
    }

    fun getAppointment(): AppointmentModel {
        /** Get appointment in database */
        val appointmentModel: AppointmentModel = AppointmentModel()
        // TODO: implement logic
        return appointmentModel
    }
}