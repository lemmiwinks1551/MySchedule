package com.example.projectnailsschedule.presentation.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.CalendarDbDeleteObj
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.StartInstagramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartPhoneUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartTelegramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartVkUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartWhatsAppUc
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarRvAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DateParamsViewModel @Inject constructor(
    private val loadShortDateUseCase: LoadShortDateUseCase,
    private val selectCalendarDateByDateUseCase: SelectCalendarDateByDateUseCase,
    private val insertCalendarDateUseCase: InsertCalendarDateUseCase,
    private val calendarDbDeleteObj: CalendarDbDeleteObj,
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {
    private val tagDateColor = "DateColor"

    // var updates when click at day or month in calendar
    var selectedDate = MutableLiveData(
        DateParams(
            date = LocalDate.now()
        )
    )

    // position of appointments in selectedDate.appointmentsList to edit
    // if position == null - create new appointment
    var appointmentPosition: Int? = null

    var text = MutableLiveData(String())

    var previousDate = MutableLiveData(DateParams())

    var dateDetailsVisibility = MutableLiveData(false)

    var prevHolder: CalendarRvAdapter.ViewHolder? = null

    suspend fun getArrayAppointments(date: LocalDate): MutableList<AppointmentModelDb> {
        return loadShortDateUseCase.execute(date)
    }

    fun updateSelectedDate(dateParams: DateParams) {
        val updatedDateParams =
            selectedDate.value?.copy(
                date = dateParams.date,
                appointmentsList = dateParams.appointmentsList
            )
        selectedDate.postValue(updatedDateParams)
    }

    fun changeMonth(operator: Boolean) {
        val updatedDateParams: DateParams = when (operator) {
            true -> selectedDate.value?.copy(
                date = selectedDate.value?.date?.plusMonths(1)
            )!!

            false -> selectedDate.value?.copy(
                date = selectedDate.value?.date?.minusMonths(1)
            )!!
        }

        selectedDate.postValue(updatedDateParams)
        dateDetailsVisibility.postValue(false)
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

    suspend fun insertAppointment(appointmentModelDb: AppointmentModelDb): Long {
        return insertAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun updateAppointment(appointmentModelDb: AppointmentModelDb) : Boolean {
        return updateAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
    }

    fun startVk(uri: String) {
        startVkUc.execute(uri)
    }

    fun startTelegram(uri: String) {
        startTelegramUc.execute(uri)
    }

    fun startInstagram(uri: String) {
        startInstagramUc.execute(uri)
    }

    fun startWhatsApp(uri: String) {
        startWhatsAppUc.execute(uri)
    }

    fun startPhone(phoneNum: String) {
        startPhoneUc.execute(phoneNum)
    }
}