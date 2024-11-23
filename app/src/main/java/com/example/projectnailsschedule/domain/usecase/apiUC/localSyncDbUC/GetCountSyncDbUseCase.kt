package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.repository.repo.ScheduleSyncRepository

class GetCountSyncDbUseCase(private var scheduleSyncRepository: ScheduleSyncRepository) {

    suspend fun execute(): Long {
        return scheduleSyncRepository.getCount()
    }
}