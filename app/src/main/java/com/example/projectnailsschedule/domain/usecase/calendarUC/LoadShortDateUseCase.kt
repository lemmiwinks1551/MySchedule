package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class LoadShortDateUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(selectedDateParams: DateParams): Array<AppointmentModelDb> {
        return scheduleRepository.getDateAppointments(dateParams = selectedDateParams)
    }
}