package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.CalendarRepository

class SelectDateUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(dateParams: DateParams): DateParams {
        return dateParams
    }
}