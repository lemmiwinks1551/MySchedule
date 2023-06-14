package com.example.projectnailsschedule.presentation.editClient

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SaveClientUseCase

class ClientEditViewModel(
    private val saveClientUseCase: SaveClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase
) : ViewModel() {

    fun saveClient(clientModelDb: ClientModelDb) {
        saveClientUseCase.execute(clientModelDb)
    }

    fun deleteClient(clientModelDb: ClientModelDb) {
        deleteClientUseCase.execute(clientModelDb)
    }
}