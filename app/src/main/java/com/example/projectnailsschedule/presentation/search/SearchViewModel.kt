package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase

class SearchViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase
) : ViewModel() {

    var scheduleDb: ScheduleDb? = null
    val appointmentCount: MutableLiveData<Int> = MutableLiveData()

    fun searchDatabase(searchQuery: String): LiveData<List<AppointmentModelDb>>? {
        return scheduleDb?.getDao()?.searchDatabase(searchQuery)?.asLiveData()
    }

    fun getAllAppointments() : LiveData<List<AppointmentModelDb>>? {
        return scheduleDb?.getDao()?.selectAll()?.asLiveData()
    }
}