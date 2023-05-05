package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase

/** Create Factory for Calendar Fragment with UseCases */

class CalendarViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private val loadShortDateUseCase =
        LoadShortDateUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var getDateAppointmentsUseCase =
        GetDateAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(
            loadShortDateUseCase,
            getDateAppointmentsUseCase
        ) as T
    }
}