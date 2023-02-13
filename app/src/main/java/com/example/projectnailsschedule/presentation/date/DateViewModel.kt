package com.example.projectnailsschedule.presentation.date

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateStatusUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetAppointmentsCountUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDateStatusUseCase

class DateViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var setDateStatusUseCase: SetDateStatusUseCase,
    private var getDateStatusUseCase: GetDateStatusUseCase,
    private var getAppointmentsCountUseCase: GetAppointmentsCountUseCase
) : ViewModel() {

    var selectedDateParams =
        MutableLiveData(
            DateParams(
                _id = null,
                date = null,
                status = null,
                appointmentCount = null
            )
        )

    fun getDateStatus() {
        selectedDateParams.value = DateParams(
            _id = selectedDateParams.value?._id,
            date = selectedDateParams.value?.date,
            status =
            getDateStatusUseCase.execute(selectedDateParams.value!!).status.toString(),
            appointmentCount = null
        )
    }

    fun getDateAppointmentCount() {
        selectedDateParams.value = DateParams(
            _id = selectedDateParams.value?._id,
            date = selectedDateParams.value?.date,
            status = selectedDateParams.value?.status,
            appointmentCount = getAppointmentsCountUseCase.execute(selectedDateParams.value!!)
        )
    }

    fun deleteAppointment(appointmentParams: AppointmentParams) {
        deleteAppointmentUseCase.execute(appointmentParams = appointmentParams)
    }

    fun setDateStatus(dateParams: DateParams) {
        getDateStatusUseCase.execute(dateParams = dateParams)
    }
}