package com.example.projectnailsschedule.presentation.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.StartInstagramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartPhoneUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartTelegramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartVkUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartWhatsAppUc
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val insertClientUseCase: InsertClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val searchClientUseCase: SearchClientUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    suspend fun insertClient(clientModelDb: ClientModelDb) {
        insertClientUseCase.execute(clientModelDb)
    }

    suspend fun updateClient(clientModelDb: ClientModelDb) {
        updateClientUseCase.execute(clientModelDb)
    }

    suspend fun deleteClient(clientModelDb: ClientModelDb) {
        deleteClientUseCase.execute(clientModelDb)
    }

    suspend fun searchClient(searchQuery: String): LiveData<List<ClientModelDb>> {
        return searchClientUseCase.execute(searchQuery)
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