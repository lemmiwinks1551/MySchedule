package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.repository.repo.ScheduleSyncRepository
import java.util.Date

class GetUserLastLocalAppointmentTimestamp(private val scheduleSyncRepository: ScheduleSyncRepository) {

    suspend fun execute(): Long? {
        return scheduleSyncRepository.getMaxAppointmentTimestamp()
    }
}