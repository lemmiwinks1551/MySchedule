package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentsUseCase
import java.time.LocalDate

class SearchViewModel(
    private val searchAppointmentsUseCase: SearchAppointmentsUseCase
) : ViewModel() {

    var appointmentArray = MutableLiveData<MutableList<AppointmentParams>>()

    fun searchAppointment() {
        // get cursor with data
        appointmentArray.value = mutableListOf()
        val cursor = searchAppointmentsUseCase.execute()

        // add data to list
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            val date = cursor.getString(1)
            val time = cursor.getString(2)
            val procedure = cursor.getString(3)
            val name = cursor.getString(4)
            val phone = cursor.getString(5)
            val misc = cursor.getString(6)

            val appointmentParams = AppointmentParams(
                _id = null,
                appointmentDate = LocalDate.parse(date),
                clientName = name,
                startTime = time,
                procedure = procedure,
                phoneNum = phone,
                misc = misc
            )
            appointmentArray.value?.add(appointmentParams)
        }
        cursor.close();
        appointmentArray.value = appointmentArray.value
    }
}