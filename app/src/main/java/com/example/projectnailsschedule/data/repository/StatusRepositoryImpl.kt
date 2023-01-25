package com.example.projectnailsschedule.data.repository

import com.example.projectnailsschedule.domain.models.StatusModel

class StatusRepositoryImpl {

    class AppointmentRepository {

        fun saveAppointment(statusModel: StatusModel): Boolean {
            /** Save appointment in database */
            return true
        }

        fun getAppointment(): StatusModel {
            /** Get appointment in database */
            val statusModel: StatusModel = StatusModel()
            // TODO: implement logic
            return statusModel
        }
    }
}