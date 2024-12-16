package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class GetOldUpdatedCalendarDateUseCase(private var calendarRepository: CalendarRepository) {

    suspend fun execute(): List<CalendarDateModelDb> {
        return calendarRepository.getOldUpdatedCalendarDate()
    }
}