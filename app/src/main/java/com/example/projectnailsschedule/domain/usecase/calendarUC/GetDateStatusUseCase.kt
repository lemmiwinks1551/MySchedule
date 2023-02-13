package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.StatusRepository

/** Load calendar with statuses from database
 * take dateParams
 * add day status
 * return dateParams */

class GetDateStatusUseCase(private val statusRepository: StatusRepository) {

    fun execute(dateParams: DateParams): DateParams {
        return statusRepository.getStatus(dateParams = dateParams)
    }
}