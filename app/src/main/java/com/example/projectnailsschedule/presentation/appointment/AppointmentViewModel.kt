package com.example.projectnailsschedule.presentation.appointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.usecase.appointmentUC.EditAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase

class AppointmentViewModel(
    private val saveAppointmentUseCase: SaveAppointmentUseCase,
    private val editAppointmentUseCase: EditAppointmentUseCase
) : ViewModel() {

    val log = this::class.simpleName

    fun createAppointment(appointmentParams: AppointmentParams) {
        saveAppointmentUseCase.execute(appointmentParams)
        Log.e(log, "Appointment saved")
    }

    fun editAppointment(appointmentParams: AppointmentParams) {
        editAppointmentUseCase.execute(appointmentParams)
        Log.e(log, "Appointment edited")
    }

    init {
        Log.e(log, "AppointmentViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(log, "AppointmentViewModel cleared")
    }
}