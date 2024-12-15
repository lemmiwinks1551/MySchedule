package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class GetBySyncUuidCalendarDateUseCase(private val calendarRepository: CalendarRepository) {

    suspend fun execute(uuid: String): CalendarDateModelDb? {
        return calendarRepository.getBySyncUUID(uuid)
    }
}