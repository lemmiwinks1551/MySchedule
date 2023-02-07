package com.example.projectnailsschedule.presentation.calendar

import android.os.Bundle
import android.util.Log
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

    fun getDayStatus(dateParams: DateParams): DateParams {
        // get day status from repository
        loadCalendarUseCase.execute(dateParams)
        return dateParams
    }

    fun changeDay(day: Int) {
        selectedDate.value = selectedDate.value?.withDayOfMonth(day)
        Log.e(log, "Chosen day ${selectedDate.value.toString()}")
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

    fun goIntoDate(): Bundle {
        // create bundle
        val bundle = Bundle()

        // crete dateParams obj with chosen date
        val dateParams = DateParams(
            _id = null,
            date = selectedDate.value,
            status = null
        )

        // put dateParams to the bundle
        bundle.putParcelable("dateParams", dateParams)
        return bundle
    }
}