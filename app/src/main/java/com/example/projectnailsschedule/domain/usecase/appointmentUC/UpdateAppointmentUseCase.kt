package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class UpdateAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(appointmentModelDb: AppointmentModelDb) {
        scheduleRepository.updateAppointment(appointmentModelDb)
    }
}