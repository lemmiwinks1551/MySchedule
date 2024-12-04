package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class DeleteCalendarDateUseCase(private val calendarRepository: CalendarRepository) {
    suspend fun execute(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return calendarRepository.delete(calendarDateModelDb = calendarDateModelDb)
    }
}