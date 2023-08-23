package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
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

    private var getSelectedMonthUc =
        GetSelectedMonthUc(settingsRepository = settingsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(
            loadShortDateUseCase,
            getDateAppointmentsUseCase,
            setSelectedMonthUc,
            getSelectedMonthUc
        ) as T
    }
}