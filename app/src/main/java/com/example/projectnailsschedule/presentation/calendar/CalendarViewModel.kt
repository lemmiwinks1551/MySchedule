package com.example.projectnailsschedule.presentation.calendar

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectNextMonthUseCase
import java.time.LocalDate

class CalendarViewModel(
    private val loadCalendarUseCase: LoadCalendarUseCase,
    private val selectDateUseCase: SelectDateUseCase,
    private val selectMonth: SelectNextMonthUseCase
) : ViewModel() {

    private val log = this::class.simpleName
    private val operatorAdd = '+'

    var selectedDate = MutableLiveData(LocalDate.now())
    var selectedMonth = MutableLiveData(LocalDate.now())
    var selectedYearValue = MutableLiveData<Int>()
    var bundleDateParams = MutableLiveData<Bundle>()

    fun getDayStatus(dateParams: DateParams): DateParams {
        // get day status from repository
        loadCalendarUseCase.execute(dateParams)
        return dateParams
    }

    fun changeDay(day: Int) {
        selectedDate.value = selectedDate.value?.withDayOfMonth(day)
    }

    fun changeMonth(operator: Char) {
        // change current month
        // change selectedMonth for CalendarRecyclerView observer
        if (operator == operatorAdd) {
            selectedDate.value = selectedDate.value?.plusMonths(1)
            selectedMonth.value = selectedMonth.value?.plusMonths(1)
        } else {
            selectedDate.value = selectedDate.value?.minusMonths(1)
            selectedMonth.value = selectedMonth.value?.minusMonths(1)
        }

        // change year if year changed
        if (selectedYearValue.value != selectedDate.value?.year) {
            selectedYearValue.value = selectedDate.value?.year
        }
    }

    fun goIntoDate() {
        // create dateParams obj with chosen date
        val dateParams = DateParams(
            _id = null,
            date = selectedDate.value,
            status = null)

        // get bundle from SelectDateUseCase
        bundleDateParams.value = selectDateUseCase.execute(dateParams)
    }
}