package com.example.projectnailsschedule.domain.usecase.calendarUC

import android.database.Cursor
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class LoadShortDateUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(selectedDateParams: DateParams): Cursor {
        return scheduleRepository.getDateAppointments(dateParams = selectedDateParams)
    }
}