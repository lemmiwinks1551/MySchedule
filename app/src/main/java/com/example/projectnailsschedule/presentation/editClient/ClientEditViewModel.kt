package com.example.projectnailsschedule.presentation.editClient

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SaveClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase

class ClientEditViewModel(
    private val saveClientUseCase: SaveClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase
) : ViewModel() {

    fun saveClient(clientModelDb: ClientModelDb) {
        saveClientUseCase.execute(clientModelDb)
    }

    fun updateClient(clientModelDb: ClientModelDb) {
        updateClientUseCase.execute(clientModelDb)
    }
}