package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.repository.AppointmentRepository

class EditAppointmentUseCase(private val appointmentRepository: AppointmentRepository) {

    fun execute(appointmentParams: AppointmentParams) {
        appointmentRepository.editAppointment(appointmentParams)
    }
}