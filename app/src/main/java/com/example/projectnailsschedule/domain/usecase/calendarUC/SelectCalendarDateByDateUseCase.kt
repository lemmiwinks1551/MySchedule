package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class SelectCalendarDateByDateUseCase(private val calendarRepository: CalendarRepository) {
    suspend fun execute(date: String): CalendarDateModelDb {
        return calendarRepository.selectDate(date = date)
    }
}