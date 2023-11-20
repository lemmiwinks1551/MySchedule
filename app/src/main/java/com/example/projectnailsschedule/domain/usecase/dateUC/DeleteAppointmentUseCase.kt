package com.example.projectnailsschedule.domain.usecase.dateUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class DeleteAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(appointmentModelDb: AppointmentModelDb): Boolean {
        scheduleRepository.deleteAppointment(appointmentModelDb)
        return true
    }
}