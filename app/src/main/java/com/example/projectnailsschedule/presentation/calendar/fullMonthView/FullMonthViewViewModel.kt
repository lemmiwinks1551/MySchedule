package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetMonthAppointmentsUC
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase

class FullMonthViewViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getMonthAppointmentsUseCase: GetMonthAppointmentsUC
) : ViewModel() {

    val log = this::class.simpleName

    fun deleteAppointments(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
    }

    fun getMonthAppointments(dateMonth: String): LiveData<List<AppointmentModelDb>> {
        return getMonthAppointmentsUseCase.execute(dateMonth)
    }
}