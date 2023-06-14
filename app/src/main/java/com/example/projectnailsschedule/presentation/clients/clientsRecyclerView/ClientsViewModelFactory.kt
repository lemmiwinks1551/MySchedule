package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel

class ClientsViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val clientsRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = clientsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClientsViewModel() as T
    }
}