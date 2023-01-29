package com.example.projectnailsschedule.presentation.calendar

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectNextMonthUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectPrevMonthUseCase

class CalendarViewModel(
    private val loadCalendarUseCase: LoadCalendarUseCase,
    private val selectDateUseCase: SelectDateUseCase,
    private val selectNextMonthUseCase: SelectNextMonthUseCase,
    private val selectPrevMonthUseCase: SelectPrevMonthUseCase
) : ViewModel() {

    val log = this::class.simpleName

    fun getDayStatus(dateParams: DateParams): DateParams {
        loadCalendarUseCase.execute(dateParams)
        return dateParams
    }

    fun selectDate() {

    }

    fun selectNextMonth() {

    }

    fun selectPrevMonth() {

    }

}