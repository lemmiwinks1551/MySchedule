package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsUseCase
import java.time.LocalDate

class SearchViewModel(
    private val getAllAppointmentsUseCase: GetAllAppointmentsUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase

) : ViewModel() {

    var appointmentArray = MutableLiveData<MutableList<AppointmentParams>>()
    var allAppointmentsCursor = getAllAppointmentsUseCase.execute()

    fun getAllAppointments() {
        // get cursor with data
        appointmentArray.value = mutableListOf()
        allAppointmentsCursor = getAllAppointmentsUseCase.execute()

        // add data to list
        allAppointmentsCursor.moveToPosition(-1)
        while (allAppointmentsCursor.moveToNext()) {
            val date = allAppointmentsCursor.getString(1)
            val time = allAppointmentsCursor.getString(2)
            val procedure = allAppointmentsCursor.getString(3)
            val name = allAppointmentsCursor.getString(4)
            val phone = allAppointmentsCursor.getString(5)
            val misc = allAppointmentsCursor.getString(6)

            val appointmentParams = AppointmentParams(
                _id = null,
                appointmentDate = LocalDate.parse(date),
                clientName = name,
                startTime = time,
                procedure = procedure,
                phoneNum = phone,
                misc = misc,
                deleted = 0
            )
            appointmentArray.value?.add(appointmentParams)
        }
        appointmentArray.value = appointmentArray.value
    }

    fun getAppointmentId(id: Int): Int {
        allAppointmentsCursor.moveToPosition(id)
        return allAppointmentsCursor.getString(0)!!.toInt()
    }

    fun deleteAppointment(id: Int) {
        getAppointmentId(id)
        deleteAppointmentUseCase.execute(allAppointmentsCursor.getString(0)!!.toInt())
    }
}