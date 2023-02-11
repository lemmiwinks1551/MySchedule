package com.example.projectnailsschedule.domain.usecase.calendarUC

import android.database.Cursor
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.models.DateParams

class LoadShortDateUseCase(private val scheduleRepositoryImpl: ScheduleRepositoryImpl) {

    fun execute(selectedDateParams: DateParams): Cursor {
        return scheduleRepositoryImpl.getDateAppointments(dateParams = selectedDateParams)
    }
}