package com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC

import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class GetMaxAppointmentsTimestamp(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(): Long? {
        return scheduleRepository.getMaxTimestamp()
    }
}