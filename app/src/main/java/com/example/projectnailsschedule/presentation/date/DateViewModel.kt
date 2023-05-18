package com.example.projectnailsschedule.presentation.date

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase

class DateViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase
    ) : ViewModel() {

    val log = this::class.simpleName
    var selectedDateParams =
        MutableLiveData(
            DateParams(
                _id = null,
                date = null,
                appointmentCount = null
            )
        )

    fun updateDateParams() {
        // set day status and appointmentsCount
        getDateAppointmentCount()
        selectedDateParams.value = selectedDateParams.value
    }

    private fun getDateAppointmentCount() {
        selectedDateParams.value?.appointmentCount =
            getDateAppointmentsUseCase.execute(selectedDateParams.value!!).size
    }

    fun getDateAppointments(): Array<AppointmentModelDb> {
        return getDateAppointmentsUseCase.execute(dateParams = selectedDateParams.value!!)
    }

    fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
        updateDateParams()
    }
}