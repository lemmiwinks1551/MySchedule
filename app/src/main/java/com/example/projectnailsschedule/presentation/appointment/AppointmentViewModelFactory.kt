package com.example.projectnailsschedule.presentation.appointment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*

class AppointmentViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private val insertAppointmentUseCase =
        InsertAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val editAppointmentUseCase =
        UpdateAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var startVkUc = StartVkUc(context!!)
    private var startTelegramUc = StartTelegramUc(context!!)
    private var startInstagramUc = StartInstagramUc(context!!)
    private var startWhatsAppUc = StartWhatsAppUc(context!!)
    private var startPhoneUc = StartPhoneUc(context!!)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentViewModel(
            insertAppointmentUseCase = insertAppointmentUseCase,
            updateAppointmentUseCase = editAppointmentUseCase,
            startVkUc = startVkUc,
            startTelegramUc = startTelegramUc,
            startInstagramUc = startInstagramUc,
            startWhatsAppUc = startWhatsAppUc,
            startPhoneUc = startPhoneUc
        ) as T
    }
}