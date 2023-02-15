package com.example.projectnailsschedule.presentation.calendar

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateStatusUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.presentation.date.DateViewModel
import java.time.LocalDate

class CalendarViewModel(
    private val getDateStatusUseCase: GetDateStatusUseCase,
    private val loadShortDateUseCase: LoadShortDateUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase
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


    fun getDayStatus(dateParams: DateParams): DateParams {
        // get day status from repository
        // use in CalendarRecyclerView
        return getDateStatusUseCase.execute(dateParams)
    }

    fun getCursorAppointments(dateParams: DateParams): Cursor {
        return loadShortDateUseCase.execute(dateParams)
    }

    private fun getDateAppointmentCount() {
        selectedDateParams.value?.appointmentCount =
            getDateAppointmentsUseCase.execute(selectedDateParams.value!!).count
        // selectedDateParams.value = selectedDateParams.value
    }

    fun changeDay(day: Int) {
        // set month day in selectedDayParams
        selectedDateParams.value?.date = selectedDateParams.value?.date?.withDayOfMonth(day)
        getDateAppointmentCount()
        selectedDateParams.value = selectedDateParams.value
    }

    fun changeMonth(operator: Char) {
        // change current month
        if (operator == operatorAdd) {
            selectedDateParams.value?.date = selectedDateParams.value?.date?.plusMonths(1)
        } else {
            selectedDateParams.value?.date = selectedDateParams.value?.date?.minusMonths(1)
        }
        selectedDateParams.value = selectedDateParams.value
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(log, "onCLeared")
    }
}