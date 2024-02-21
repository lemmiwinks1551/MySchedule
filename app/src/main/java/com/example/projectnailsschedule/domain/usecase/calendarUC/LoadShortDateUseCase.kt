package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import java.time.LocalDate

class LoadShortDateUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(date: LocalDate): MutableList<AppointmentModelDb> {
        return scheduleRepository.getDateAppointments(date = date)
    }
}