package com.example.projectnailsschedule.presentation.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.CalendarDbDeleteObj
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarRvAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadShortDateUseCase: LoadShortDateUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase,
    private var setSelectedMonthUc: SetSelectedMonthUc,
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val selectCalendarDateByDateUseCase: SelectCalendarDateByDateUseCase,
    private val insertCalendarDateUseCase: InsertCalendarDateUseCase,
    private val calendarDbDeleteObj: CalendarDbDeleteObj
) : ViewModel() {

    private val tagDateColor = "DateColor"

    // var updates when click at day or month in calendar
    var selectedDate = MutableLiveData(
        DateParams(
            _id = null,
            date = LocalDate.now(),
            appointments = null,
            appointmentsArray = null
        )
    )

    var visibility = MutableLiveData(false)

    var prevHolder: CalendarRvAdapter.ViewHolder? = null

    suspend fun getArrayAppointments(date: LocalDate): Array<AppointmentModelDb> {
        return loadShortDateUseCase.execute(date)
    }

    fun updateSelectedDate(dateParams: DateParams) {
        val updatedDateParams =
            selectedDate.value?.copy(
                date = dateParams.date,
                appointmentsArray = dateParams.appointmentsArray
            )
        selectedDate.postValue(updatedDateParams)
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

    fun getUserTheme(): String {
        return getUserThemeUseCase.execute()
    }

    private suspend fun selectCalendarDateByDate(date: String): CalendarDateModelDb {
        return selectCalendarDateByDateUseCase.execute(date)
    }

    suspend fun getDateId(ruFormatDate: String): Int? {
        Log.d(tagDateColor, "Getting color for $ruFormatDate")
        return try {
            val deferredColor = CoroutineScope(Dispatchers.IO).async {
                selectCalendarDateByDate(ruFormatDate)
            }

            Log.d(tagDateColor, "Date $ruFormatDate has color ${deferredColor.await()._id}")
            deferredColor.await()._id
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getDateColor(ruFormatDate: String): String? {
        Log.d(tagDateColor, "Getting color for $ruFormatDate")

        val deferredColor = CoroutineScope(Dispatchers.IO).async {
            selectCalendarDateByDate(ruFormatDate)
        }

        Log.d(tagDateColor, "Date $ruFormatDate has color ${deferredColor.await().color}")
        return deferredColor.await().color
    }

    suspend fun insertCalendarDate(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return insertCalendarDateUseCase.execute(calendarDateModelDb)
    }

    suspend fun calendarDbDeleteObj(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return calendarDbDeleteObj.execute(calendarDateModelDb)
    }
}