package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.StatusRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase

/** Create Factory for Calendar Fragment with UseCases */

class CalendarViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val statusRepositoryImpl = StatusRepositoryImpl(context = context)
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context)

    private val loadCalendarUseCase =
        LoadCalendarUseCase(statusRepository = statusRepositoryImpl)

    private val loadShortDateUseCase =
        LoadShortDateUseCase(scheduleRepositoryImpl = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(
            loadCalendarUseCase,
            loadShortDateUseCase
        ) as T
    }
}