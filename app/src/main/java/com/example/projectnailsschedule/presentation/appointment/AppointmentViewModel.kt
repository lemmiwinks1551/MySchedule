package com.example.projectnailsschedule.presentation.appointment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*

class AppointmentViewModel(
    private val saveAppointmentUseCase: SaveAppointmentUseCase,
    private val editAppointmentUseCase: UpdateAppointmentUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    val log = this::class.simpleName

    fun createAppointment(appointmentModelDb: AppointmentModelDb) {
        saveAppointmentUseCase.execute(appointmentModelDb)
        Log.e(log, "Appointment saved")
    }

    fun editAppointment(appointmentModelDb: AppointmentModelDb) {
        editAppointmentUseCase.execute(appointmentModelDb)
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