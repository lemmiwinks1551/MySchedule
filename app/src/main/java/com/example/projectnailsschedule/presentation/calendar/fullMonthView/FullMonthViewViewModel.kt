package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*
import java.time.LocalDate

class FullMonthViewViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase,
    private var setSelectedMonthUc: SetSelectedMonthUc,
    private var getSelectedMonthUc: GetSelectedMonthUc,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    val log = this::class.simpleName
    val selectedMonth = MutableLiveData<LocalDate>()
    var oldPosition: Int = 0

    init {
        selectedMonth.value = getSelectedMonth()
    }

    fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
    }

    fun saveAppointment(appointmentModelDb: AppointmentModelDb) {
        insertAppointmentUseCase.execute(appointmentModelDb)
    }

    fun getDateAppointments(dateParams: DateParams): MutableList<AppointmentModelDb> {
        return getDateAppointmentsUseCase.execute(dateParams).toMutableList()
    }

    fun changeMonth(operator: Boolean) {
        // change current month
        when (operator) {
            true -> selectedMonth.value = selectedMonth.value?.plusMonths(1)
            false -> selectedMonth.value = selectedMonth.value?.minusMonths(1)
        }
        setSelectedMonth(selectedMonth.value!!)
    }

    private fun getSelectedMonth(): LocalDate {
        return getSelectedMonthUc.execute()
    }

    private fun setSelectedMonth(date: LocalDate) {
        setSelectedMonthUc.execute(date)
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