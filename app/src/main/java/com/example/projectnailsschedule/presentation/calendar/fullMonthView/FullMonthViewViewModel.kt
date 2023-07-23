package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetMonthAppointmentsUC
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import java.time.LocalDate

class FullMonthViewViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getMonthAppointmentsUseCase: GetMonthAppointmentsUC,
    private var saveAppointmentUseCase: SaveAppointmentUseCase
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

    fun getMonthAppointments(dateMonth: String): List<AppointmentModelDb> {
        return getMonthAppointmentsUseCase.execute(dateMonth)
    }

    fun changeMonth(operator: Boolean) {
        // change current month
        when (operator) {
            true -> selectedMonth.value = selectedMonth.value?.plusMonths(1)
            false -> selectedMonth.value = selectedMonth.value?.minusMonths(1)
        }
    }
}