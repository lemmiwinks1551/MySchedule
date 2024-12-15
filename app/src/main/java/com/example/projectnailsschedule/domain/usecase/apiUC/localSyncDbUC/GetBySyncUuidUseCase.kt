package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class GetBySyncUuidUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(uuid: String): AppointmentModelDb? {
        return scheduleRepository.getBySyncUUID(uuid)
    }
}