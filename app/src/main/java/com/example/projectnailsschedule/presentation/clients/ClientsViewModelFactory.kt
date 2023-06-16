package com.example.projectnailsschedule.presentation.clients

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ClientRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SaveClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase

class ClientsViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val clientsRepositoryImpl = ClientRepositoryImpl(context = context!!)

    private var searchClientUseCase = SearchClientUseCase(clientsRepository = clientsRepositoryImpl)
    private var deleteClientUseCase = DeleteClientUseCase(clientsRepository = clientsRepositoryImpl)
    private var saveClientUseCase = SaveClientUseCase(clientsRepository = clientsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClientsViewModel(
            searchClientUseCase = searchClientUseCase,
            deleteClientUseCase = deleteClientUseCase,
            saveClientUseCase = saveClientUseCase
        ) as T
    }
}