package com.example.projectnailsschedule.presentation.appointment.selectClient

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ClientRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase

class SelectClientViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val clientsRepositoryImpl = ClientRepositoryImpl(context = context!!)

    private var searchClientUseCase = SearchClientUseCase(clientsRepository = clientsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectClientViewModel(
            searchClientUseCase = searchClientUseCase
        ) as T
    }
}