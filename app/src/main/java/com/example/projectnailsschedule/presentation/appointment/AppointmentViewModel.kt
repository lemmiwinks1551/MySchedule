package com.example.projectnailsschedule.presentation.appointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.usecase.SaveAppointmentUseCase

class AppointmentViewModel(
    private val saveAppointmentUseCase: SaveAppointmentUseCase,
    private val loadAppointmentUseCase: SaveAppointmentUseCase
) : ViewModel() {

    val log = this::class.simpleName

    fun saveAppointment(appointmentParams: AppointmentParams) {
        /** Repository and UseCases */
        saveAppointmentUseCase.execute(appointmentParams)
    }

    fun loadAppointment() {
    }

    init {
        Log.e(log, "AppointmentViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(log, "AppointmentViewModel cleared")
    }
}