package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class GetByIdCalendarDateUseCase(private val calendarRepository: CalendarRepository) {

    suspend fun execute(id: Long): CalendarDateModelDb? {
        return calendarRepository.getByLocalId(id)
    }
}