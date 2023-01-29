package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.CalendarRepository

/** Load calendar with statuses from database
 * take dateParams
 * add day status
 * return dateParams */

class LoadCalendarUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(dateParams: DateParams): DateParams {
        return calendarRepository.getDate(dateParams = dateParams)
    }
}