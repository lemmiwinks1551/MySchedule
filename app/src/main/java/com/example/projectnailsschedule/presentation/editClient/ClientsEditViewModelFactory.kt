package com.example.projectnailsschedule.presentation.editClient

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ClientRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SaveClientUseCase

class ClientsEditViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val clientsRepositoryImpl = ClientRepositoryImpl(context = context!!)

    private var saveClientUseCase = SaveClientUseCase(clientsRepository = clientsRepositoryImpl)
    private var deleteClientUseCase = DeleteClientUseCase(clientsRepository = clientsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClientEditViewModel(
            saveClientUseCase = saveClientUseCase,
            deleteClientUseCase = deleteClientUseCase
        ) as T
    }
}