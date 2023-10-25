package com.example.projectnailsschedule.presentation.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*

class ClientsViewModel(
    private val searchClientUseCase: SearchClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val insertClientUseCase: InsertClientUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    fun searchDatabase(searchQuery: String): LiveData<List<ClientModelDb>> {
        return searchClientUseCase.execute(searchQuery)
    }

    fun deleteClient(clientModelDb: ClientModelDb) {
        deleteClientUseCase.execute(clientModelDb)
    }

    fun saveClient(clientModelDb: ClientModelDb) {
        insertClientUseCase.execute(clientModelDb)
    }

    fun startVk(uri: String) {
        startVkUc.execute(uri)
    }

    fun startTelegram(uri: String) {
        startTelegramUc.execute(uri)
    }

    fun startInstagram(uri: String) {
        startInstagramUc.execute(uri)
    }

    fun startWhatsApp(uri: String) {
        startWhatsAppUc.execute(uri)
    }

    fun startPhone(phoneNum: String) {
        startPhoneUc.execute(phoneNum)
    }
}