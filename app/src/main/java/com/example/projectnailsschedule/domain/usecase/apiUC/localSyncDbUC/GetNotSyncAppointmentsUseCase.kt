package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class GetNotSyncAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(): List<AppointmentModelDb> {
        return scheduleRepository.getNotSyncAppointments()
    }
}