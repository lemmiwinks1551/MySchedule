package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class InsertCalendarDateUseCase(private val calendarRepository: CalendarRepository) {
    suspend fun execute(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return calendarRepository.insertDate(calendarDateModelDb = calendarDateModelDb)
    }
}