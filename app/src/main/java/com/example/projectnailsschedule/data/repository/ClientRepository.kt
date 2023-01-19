package com.example.projectnailsschedule.data.repository

import com.example.projectnailsschedule.domain.models.ClientModel

class ClientRepository {

    class AppointmentRepository {

        fun saveAppointment(clientModel: ClientModel): Boolean {
            /** Save appointment in database */
            return true
        }

        fun getAppointment(): ClientModel {
            /** Get appointment in database */
            val clientModel: ClientModel = ClientModel()
            // TODO: implement logic
            return clientModel
        }
    }
}