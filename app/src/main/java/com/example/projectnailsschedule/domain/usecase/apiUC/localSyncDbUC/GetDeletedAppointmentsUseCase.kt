package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.repository.repo.ScheduleSyncRepository

class GetDeletedAppointmentsUseCase(private val scheduleSyncRepository: ScheduleSyncRepository) {

    suspend fun execute(): List<AppointmentDto> {
        return scheduleSyncRepository.getDeletedAppointments()
    }
}