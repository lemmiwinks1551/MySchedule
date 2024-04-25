package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class UpdateClientInAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(clientModelDb: ClientModelDb): Boolean {
        scheduleRepository.updateClientInAppointments(clientModelDb)
        return true
    }
}