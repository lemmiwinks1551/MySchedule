package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.CalendarRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectNextMonthUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectPrevMonthUseCase

/** Create Factory for Calendar Fragment with UseCases */

class CalendarViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val calendarRepository = CalendarRepositoryImpl(context = context)

    private val loadCalendarUseCase =
        LoadCalendarUseCase(calendarRepository = calendarRepository)

    private val selectDateUseCase =
        SelectDateUseCase(calendarRepository = calendarRepository)

    private val selectNextMonthUseCase =
        SelectNextMonthUseCase(calendarRepository = calendarRepository)

    private val selectPrevMonthUseCase =
        SelectPrevMonthUseCase(calendarRepository = calendarRepository)


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(
            loadCalendarUseCase,
            selectDateUseCase,
            selectNextMonthUseCase,
            selectPrevMonthUseCase
        ) as T
    }
}