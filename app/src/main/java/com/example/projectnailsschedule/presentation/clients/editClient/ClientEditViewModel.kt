package com.example.projectnailsschedule.presentation.clients.editClient

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientEditViewModel @Inject constructor(
    private val insertClientUseCase: InsertClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase
) : ViewModel() {

    fun saveClient(clientModelDb: ClientModelDb) {
        insertClientUseCase.execute(clientModelDb)
    }

    fun updateClient(clientModelDb: ClientModelDb) {
        updateClientUseCase.execute(clientModelDb)
    }
}