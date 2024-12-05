package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class GetCountSyncDbUseCase(private var scheduleRepository: ScheduleRepository) {

    suspend fun execute(): Long {
        return scheduleRepository.getCount()
    }
}