package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class DeleteAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(appointmentModelDb: AppointmentModelDb): Boolean {
        scheduleRepository.deleteAppointment(appointmentModelDb)
        return true
    }
}