package com.example.projectnailsschedule.presentation.calendar

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectNextMonthUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectPrevMonthUseCase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CalendarViewModel(
    private val loadCalendarUseCase: LoadCalendarUseCase,
    private val selectDateUseCase: SelectDateUseCase,
    private val selectNextMonthUseCase: SelectNextMonthUseCase,
    private val selectPrevMonthUseCase: SelectPrevMonthUseCase
) : ViewModel() {

    private val log = this::class.simpleName
    var currentMonth: LocalDate = LocalDate.now()

    var day = ""
    var month = ""
    var year = ""
    var extraMonth: Long = 0

    fun getDayStatus(dateParams: DateParams): DateParams {
        loadCalendarUseCase.execute(dateParams)
        return dateParams
    }

    fun selectDate() {

    }

    fun changeMonth(operator: Char) {
        if (operator == '+') {
            currentMonth = currentMonth.plusMonths(1)
            CalendarAdapter.month++ // ??
            extraMonth++
        } else {
            currentMonth = currentMonth.minusMonths(1)
            CalendarAdapter.month-- // ??
            extraMonth--
        }
    }

    fun getMonthYearName() : String {
        val date = Date.from(currentMonth?.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
        val month = SimpleDateFormat("LLLL", Locale.getDefault()).format(date)
        val year: String = currentMonth?.year.toString()
        return "$month $year"
    }

}