package com.example.projectnailsschedule.presentation.date

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDateStatusUseCase
import java.time.LocalDate

class DateViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var setDateStatusUseCase: SetDateStatusUseCase
) : ViewModel() {

    var selectedDateParams =
        MutableLiveData(
            DateParams(
                _id = null,
                date = LocalDate.now(),
                status = null,
                appointmentCount = null
            )
        )

    fun getDateStatus() {
        selectedDateParams
    }

    fun deleteAppointment(appointmentParams: AppointmentParams) {
        deleteAppointmentUseCase.execute(appointmentParams = appointmentParams)
    }

    fun setDateStatus(dateParams: DateParams) {
        setDateStatusUseCase.execute(dateParams = dateParams)
    }
}