package com.example.projectnailsschedule.domain.usecase.calendarUC

import android.database.Cursor
import com.example.projectnailsschedule.data.repository.AppointmentRepositoryImpl
import com.example.projectnailsschedule.domain.models.DateParams

class LoadShortDateUseCase(private val appointmentRepositoryImpl: AppointmentRepositoryImpl) {

    fun execute(selectedDateParams: DateParams): Cursor {
        return appointmentRepositoryImpl.getDateAppointments(dateParams = selectedDateParams)
    }
}