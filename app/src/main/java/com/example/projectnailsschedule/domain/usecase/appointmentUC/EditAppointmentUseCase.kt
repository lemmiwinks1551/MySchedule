package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class EditAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(appointmentParams: AppointmentParams) {
        scheduleRepository.editAppointment(appointmentParams)
    }
}