package com.example.projectnailsschedule.presentation.calendar.listMonthView

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*

class ListMonthViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val insertAppointmentUseCase =
        InsertAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val getDateAppointmentsUseCase =
        GetDateAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var setSelectedMonthUc =
        SetSelectedMonthUc(settingsRepository = settingsRepositoryImpl)

    private var getSelectedMonthUc =
        GetSelectedMonthUc(settingsRepository = settingsRepositoryImpl)

    private var startVkUc = StartVkUc(context!!)
    private var startTelegramUc = StartTelegramUc(context!!)
    private var startInstagramUc = StartInstagramUc(context!!)
    private var startWhatsAppUc = StartWhatsAppUc(context!!)
    private var startPhoneUc = StartPhoneUc(context!!)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListMonthViewModel(
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            insertAppointmentUseCase = insertAppointmentUseCase,
            getDateAppointmentsUseCase = getDateAppointmentsUseCase,
            setSelectedMonthUc = setSelectedMonthUc,
            getSelectedMonthUc = getSelectedMonthUc,
            startVkUc = startVkUc,
            startTelegramUc = startTelegramUc,
            startInstagramUc = startInstagramUc,
            startWhatsAppUc = startWhatsAppUc,
            startPhoneUc = startPhoneUc
        ) as T
    }
}