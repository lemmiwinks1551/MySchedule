package com.example.projectnailsschedule.presentation.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb

class SearchViewModel(
    context: Context
) : ViewModel() {

    var scheduleDb = ScheduleDb.getDb(context)
    val appointmentCount: MutableLiveData<Int> = MutableLiveData()

    fun searchDatabase(searchQuery: String): LiveData<List<AppointmentModelDb>> {
        return scheduleDb.getDao().searchDatabase(searchQuery).asLiveData()
    }

    fun getAllAppointments() : LiveData<List<AppointmentModelDb>> {
        return scheduleDb.getDao().selectAll().asLiveData()
    }
}