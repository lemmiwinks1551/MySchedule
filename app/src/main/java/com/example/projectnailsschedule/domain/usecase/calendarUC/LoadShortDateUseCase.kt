package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.StatusRepository

class LoadShortDateUseCase(private val statusRepository: StatusRepository) {
    fun execute(dateParams: DateParams): DateParams {
        return statusRepository.getStatus(dateParams = dateParams)
    }
}