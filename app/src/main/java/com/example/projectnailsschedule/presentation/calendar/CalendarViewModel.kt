package com.example.projectnailsschedule.presentation.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.*
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarRvAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadShortDateUseCase: LoadShortDateUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase,
    private var setSelectedMonthUc: SetSelectedMonthUc
) : ViewModel() {

    private val log = this::class.simpleName

    // var updates when click at day or month in calendar
    var selectedDate = MutableLiveData(
        DateParams(
            _id = null,
            date = LocalDate.now(),
            appointmentCount = null
        )
    )

    var visibility = MutableLiveData(false)

    var prevHolder: CalendarRvAdapter.ViewHolder? = null

    fun getArrayAppointments(dateParams: DateParams): Array<AppointmentModelDb> {
        // get all appointments in selectedDate for recycler view adapter
        Log.e(log, "Appointments from $selectedDate unloaded from DB")
        return loadShortDateUseCase.execute(dateParams)
    }

    private fun getDateAppointmentCount() {
        // get appointments count from selectedDate
        selectedDate.value?.appointmentCount =
            getDateAppointmentsUseCase.execute(selectedDate.value!!).size
        selectedDate.value = selectedDate.value
    }

    fun updateSelectedDate(day: Int) {
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

        // set date in shared prefs
        setSelectedMonth(selectedDate.value?.date!!)
    }

    private fun setSelectedMonth(date: LocalDate) {
        setSelectedMonthUc.execute(date)
    }
}