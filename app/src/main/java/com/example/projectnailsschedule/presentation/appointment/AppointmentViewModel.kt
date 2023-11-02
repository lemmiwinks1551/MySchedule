package com.example.projectnailsschedule.presentation.appointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val insertAppointmentUseCase: InsertAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    val log = this::class.simpleName

    fun createAppointment(appointmentModelDb: AppointmentModelDb) {
        insertAppointmentUseCase.execute(appointmentModelDb)
        Log.e(log, "Appointment saved")
    }

    fun editAppointment(appointmentModelDb: AppointmentModelDb) {
        updateAppointmentUseCase.execute(appointmentModelDb)
        Log.e(log, "Appointment edited")
    }

    init {
        Log.e(log, "AppointmentViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(log, "AppointmentViewModel cleared")
    }

    fun startVk(uri: String) {
        startVkUc.execute(uri)
    }

    fun startTelegram(uri: String) {
        startTelegramUc.execute(uri)
    }

    fun startInstagram(uri: String) {
        startInstagramUc.execute(uri)
    }

    fun startWhatsApp(uri: String) {
        startWhatsAppUc.execute(uri)
    }

    fun startPhone(phoneNum: String) {
        startPhoneUc.execute(phoneNum)
    }
}