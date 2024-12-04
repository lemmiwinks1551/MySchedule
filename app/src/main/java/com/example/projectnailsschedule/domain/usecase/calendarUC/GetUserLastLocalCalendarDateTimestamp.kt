package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository

class GetUserLastLocalCalendarDateTimestamp(private val calendarRepository: CalendarRepository) {

    suspend fun execute(): Long? {
        return calendarRepository.getMaxTimestamp()
    }
}