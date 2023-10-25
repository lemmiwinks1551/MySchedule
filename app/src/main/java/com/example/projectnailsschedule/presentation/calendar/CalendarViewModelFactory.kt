package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.*
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase

class CalendarViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context!!)

    private val loadShortDateUseCase =
        LoadShortDateUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var getDateAppointmentsUseCase =
        GetDateAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var setSelectedMonthUc =
        SetSelectedMonthUc(settingsRepository = settingsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(
            loadShortDateUseCase,
            getDateAppointmentsUseCase,
            setSelectedMonthUc
        ) as T
    }
}