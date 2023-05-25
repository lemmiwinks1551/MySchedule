package com.example.projectnailsschedule.presentation.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import java.time.LocalDate

class CalendarViewModel(
    private val loadShortDateUseCase: LoadShortDateUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase
) : ViewModel() {

    private val log = this::class.simpleName

    // var updates when click at day or month in calendar
    var selectedDate =
        MutableLiveData(
            DateParams(
                date = LocalDate.now()
            )
        )

    fun getArrayAppointments(dateParams: DateParams): Array<AppointmentModelDb> {
        // get all appointments in selectedDate
        // for recycler view adapter
        Log.e(log, "Appointments from $selectedDate unloaded from DB")
        return loadShortDateUseCase.execute(dateParams)
    }

    private fun getDateAppointmentCount() {
        // get appointments count form selectedDate
        selectedDate.value?.appointmentCount =
            getDateAppointmentsUseCase.execute(selectedDate.value!!).size
        selectedDate.value = selectedDate.value
    }

    fun changeDay(day: Int) {
        // set month day in selectedDayParams
        selectedDate.value?.date = selectedDate.value?.date?.withDayOfMonth(day)
        getDateAppointmentCount()
        selectedDate.value = selectedDate.value
    }

    fun changeMonth(operator: Boolean) {
        // change current month
        when (operator) {
            true -> selectedDate.value?.date = selectedDate.value?.date?.plusMonths(1)
            false -> selectedDate.value?.date = selectedDate.value?.date?.minusMonths(1)
        }
        selectedDate.value = selectedDate.value
    }
}