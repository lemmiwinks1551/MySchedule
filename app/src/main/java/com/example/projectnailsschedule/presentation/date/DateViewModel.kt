package com.example.projectnailsschedule.presentation.date

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateStatusUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDateStatusUseCase

class DateViewModel(
    private var setDateStatusUseCase: SetDateStatusUseCase,
    private var getDateStatusUseCase: GetDateStatusUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getDateAppointmentsUseCase: GetDateAppointmentsUseCase
    ) : ViewModel() {

    val log = this::class.simpleName
    var selectedDateParams =
        MutableLiveData(
            DateParams(
                _id = null,
                date = null,
                status = null,
                appointmentCount = null
            )
        )

    fun updateDateParams() {
        // set day status and appointmentsCount
        getDateStatus()
        getDateAppointmentCount()
        selectedDateParams.value = selectedDateParams.value
    }

    private fun getDateStatus() {
        selectedDateParams.value?.status =
            getDateStatusUseCase.execute(selectedDateParams.value!!).status.toString()
    }

    private fun getDateAppointmentCount() {
        selectedDateParams.value?.appointmentCount =
            getDateAppointmentsUseCase.execute(selectedDateParams.value!!).count
    }

    fun getDateAppointments(): Cursor {
        return getDateAppointmentsUseCase.execute(dateParams = selectedDateParams.value!!)
    }

    fun deleteAppointment(appointmentParams: AppointmentParams) {
        deleteAppointmentUseCase.execute(appointmentParams = appointmentParams)
    }

    fun setDateStatus(dateParams: DateParams) {
        getDateStatusUseCase.execute(dateParams = dateParams)
    }
}