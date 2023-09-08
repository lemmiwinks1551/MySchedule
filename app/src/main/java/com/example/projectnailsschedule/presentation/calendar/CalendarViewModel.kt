package com.example.projectnailsschedule.presentation.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.*
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import java.time.LocalDate

class CalendarViewModel(
    private val loadShortDateUseCase: LoadShortDateUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase,
    private var setSelectedMonthUc: SetSelectedMonthUc,
    private var getSelectedMonthUc: GetSelectedMonthUc,
    private var setSelectedDateUc: SetSelectedDateUc,
    private var getSelectedDateUc: GetSelectedDateUc
) : ViewModel() {

    private val log = this::class.simpleName

    // var updates when click at day or month in calendar
    var localSelectedDate =
        MutableLiveData(
            DateParams(
                date = getSelectedMonth()
            )
        )

    fun setMonth(date: LocalDate) {
        localSelectedDate.value?.date = date
        localSelectedDate.value = localSelectedDate.value
    }

    fun getArrayAppointments(dateParams: DateParams): Array<AppointmentModelDb> {
        // get all appointments in selectedDate
        // for recycler view adapter
        Log.e(log, "Appointments from $localSelectedDate unloaded from DB")
        return loadShortDateUseCase.execute(dateParams)
    }

    private fun getDateAppointmentCount() {
        // get appointments count form selectedDate
        localSelectedDate.value?.appointmentCount =
            getDateAppointmentsUseCase.execute(localSelectedDate.value!!).size
        localSelectedDate.value = localSelectedDate.value
    }

    fun changeDay(day: Int) {
        // set month day in selectedDayParams
        localSelectedDate.value?.date = localSelectedDate.value?.date?.withDayOfMonth(day)
        getDateAppointmentCount()
        localSelectedDate.value = localSelectedDate.value

        // set date in shared prefs
        setSelectedMonth(localSelectedDate.value?.date!!)
    }

    fun changeMonth(operator: Boolean) {
        // change current month
        when (operator) {
            true -> localSelectedDate.value?.date = localSelectedDate.value?.date?.plusMonths(1)
            false -> localSelectedDate.value?.date = localSelectedDate.value?.date?.minusMonths(1)
        }
        localSelectedDate.value = localSelectedDate.value

        // set date in shared prefs
        setSelectedMonth(localSelectedDate.value?.date!!)
    }

    fun getSelectedMonth(): LocalDate {
        return getSelectedMonthUc.execute()
    }

    private fun setSelectedMonth(date: LocalDate) {
        setSelectedMonthUc.execute(date)
    }

    fun setSelectedDateUc(selectedDate: LocalDate) {
        setSelectedDateUc.execute(selectedDate)
    }

    fun getSelectedDateUc(): LocalDate {
        return getSelectedDateUc.execute()
    }
}