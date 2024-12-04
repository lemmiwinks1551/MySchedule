package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class GetCountCalendarDateUseCase(private val calendarRepository: CalendarRepository) {

    suspend fun execute(): Long {
        return calendarRepository.getCount()
    }
}