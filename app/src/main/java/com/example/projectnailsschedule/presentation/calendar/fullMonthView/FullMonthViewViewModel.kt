package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetMonthAppointmentsUC
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import java.time.LocalDate

class FullMonthViewViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getMonthAppointmentsUseCase: GetMonthAppointmentsUC,
    private var saveAppointmentUseCase: SaveAppointmentUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase,
    private var setSelectedMonthUc: SetSelectedMonthUc,
    private var getSelectedMonthUc: GetSelectedMonthUc
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
        saveAppointmentUseCase.execute(appointmentModelDb)
    }

    fun getMonthAppointments(dateMonth: String): MutableList<AppointmentModelDb> {
        return getMonthAppointmentsUseCase.execute(dateMonth)
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
}