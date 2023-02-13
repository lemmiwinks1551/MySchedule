package com.example.projectnailsschedule.domain.usecase.dateUC

import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class GetAppointmentsCountUseCase(private val scheduleRepository: ScheduleRepository) {
    fun execute(dateParams: DateParams) : Int {
        return scheduleRepository.getDateAppointments(dateParams).count
    }
}