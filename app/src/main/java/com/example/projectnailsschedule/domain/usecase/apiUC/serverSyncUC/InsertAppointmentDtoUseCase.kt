package com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.repository.repo.ScheduleSyncRepository

class InsertAppointmentDtoUseCase(private val scheduleSyncRepository: ScheduleSyncRepository) {

    suspend fun execute(appointmentDto: AppointmentDto) {
        scheduleSyncRepository.insert(appointmentDto)
    }
}