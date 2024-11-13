package com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC

import com.example.projectnailsschedule.domain.repository.repo.ScheduleSyncRepository
import java.util.Date

class GetLastLocalAppointmentTimestamp(private val scheduleSyncRepository: ScheduleSyncRepository) {

    suspend fun execute(): Date? {
        return scheduleSyncRepository.getMaxAppointmentTimestamp()
    }
}