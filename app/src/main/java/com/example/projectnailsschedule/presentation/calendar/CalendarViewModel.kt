package com.example.projectnailsschedule.presentation.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import java.time.LocalDate

class CalendarViewModel(
    private val loadCalendarUseCase: LoadCalendarUseCase,
    private val loadShortDateUseCase: LoadShortDateUseCase
) : ViewModel() {

    private val log = this::class.simpleName
    private val operatorAdd = '+'

    var selectedDateParams =
        MutableLiveData(
            DateParams(
                _id = null,
                date = LocalDate.now(),
                status = null,
                appointmentCount = null
            )
        )

    var selectedMonth = MutableLiveData(LocalDate.now())
    var selectedYearValue = MutableLiveData(LocalDate.now().year)
    var dateParamsChangeDay = MutableLiveData<DateParams>()

    fun getDayStatus(dateParams: DateParams): DateParams {
        // get day status from repository
        // use in CalendarRecyclerView
        return loadCalendarUseCase.execute(dateParams)
    }

    fun setDayAppointmentsShort(dateParams: DateParams): DateParams {
        // get day appointments short from repository
        return loadShortDateUseCase.execute(dateParams)
    }

    fun changeDay(day: Int) {
        // set month day in selectedDayParams
        selectedDateParams.value = DateParams(
            _id = selectedDateParams.value?._id,
            date = selectedDateParams.value?.date?.withDayOfMonth(day),
            status = selectedDateParams.value?.status,
            appointmentCount = selectedDateParams.value?.appointmentCount
        )
    }

    fun changeMonth(operator: Char) {
        // change current month
        // change selectedMonth for CalendarRecyclerView observer
        if (operator == operatorAdd) {
            selectedDateParams.value = DateParams(
                _id = selectedDateParams.value?._id,
                date = selectedDateParams.value?.date?.plusMonths(1),
                status = selectedDateParams.value?.status,
                appointmentCount = selectedDateParams.value?.appointmentCount
            )

            selectedMonth.value = selectedMonth.value?.plusMonths(1)
        } else {

            selectedDateParams.value = DateParams(
                _id = selectedDateParams.value?._id,
                date = selectedDateParams.value?.date?.minusMonths(1),
                status = selectedDateParams.value?.status,
                appointmentCount = selectedDateParams.value?.appointmentCount
            )
            selectedMonth.value = selectedMonth.value?.minusMonths(1)
        }

        // change year if necessary
        if (selectedYearValue.value != selectedDateParams.value?.date?.year) {
            selectedYearValue.value = selectedDateParams.value?.date?.year
        }
    }

    fun goIntoDate() {
        // create dateParams obj with chosen date
        dateParamsChangeDay.value = DateParams(
            _id = null,
            date = selectedDateParams.value?.date,
            status = null
        )
    }
}