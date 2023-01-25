package com.example.projectnailsschedule.domain.usecase

import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.repository.AppointmentRepository

/** Save appointment in database */
class SaveAppointmentUseCase(private val appointmentRepository: AppointmentRepository) {

    fun execute(appointmentParams: AppointmentParams) {
        appointmentRepository.saveAppointment(appointmentParams)
    }
}