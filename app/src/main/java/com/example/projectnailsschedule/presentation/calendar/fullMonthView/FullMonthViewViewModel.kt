package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetMonthAppointmentsUC
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import java.time.LocalDate

class FullMonthViewViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getMonthAppointmentsUseCase: GetMonthAppointmentsUC,
    private var saveAppointmentUseCase: SaveAppointmentUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase

) : ViewModel() {

    val log = this::class.simpleName
    val selectedMonth = MutableLiveData<LocalDate>()


    init {
        selectedMonth.value = LocalDate.now()
    }

    fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
        selectedMonth.value = selectedMonth.value // update appointments
    }

    fun saveAppointment(appointmentModelDb: AppointmentModelDb) {
        saveAppointmentUseCase.execute(appointmentModelDb)
        selectedMonth.value = selectedMonth.value // update appointments
    }

    fun getMonthAppointments(dateMonth: String): MutableList<AppointmentModelDb> {
        return getMonthAppointmentsUseCase.execute(dateMonth)
    }

    fun getDateAppointments(dateParams: DateParams): Array<AppointmentModelDb> {
        return getDateAppointmentsUseCase.execute(dateParams)
    }

    fun changeMonth(operator: Boolean) {
        // change current month
        when (operator) {
            true -> selectedMonth.value = selectedMonth.value?.plusMonths(1)
            false -> selectedMonth.value = selectedMonth.value?.minusMonths(1)
        }
    }
}