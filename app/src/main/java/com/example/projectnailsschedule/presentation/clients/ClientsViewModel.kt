package com.example.projectnailsschedule.presentation.clients

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateClientInAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.GetClientByIdUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.StartInstagramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartPhoneUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartTelegramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartVkUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartWhatsAppUc
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val insertClientUseCase: InsertClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val searchClientUseCase: SearchClientUseCase,
    private val getClientByIdUseCase: GetClientByIdUseCase,
    private val updateClientInAppointmentsUseCase: UpdateClientInAppointmentsUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc,
    private var updateUserDataUseCase: UpdateUserDataUseCase
) : ViewModel() {

    var selectedClient: ClientModelDb? = null

    suspend fun insertClient(clientModelDb: ClientModelDb): Long {
        return insertClientUseCase.execute(clientModelDb)
    }

    suspend fun updateClient(clientModelDb: ClientModelDb) {
        updateClientUseCase.execute(clientModelDb)
    }

    suspend fun deleteClient(clientModelDb: ClientModelDb) {
        deleteClientUseCase.execute(clientModelDb)
    }

    suspend fun searchClient(searchQuery: String): MutableList<ClientModelDb> {
        return searchClientUseCase.execute(searchQuery)
    }

    suspend fun getClientById(id: Long): ClientModelDb {
        return getClientByIdUseCase.execute(id)
    }

    suspend fun updateClientInAppointments(clientModelDb: ClientModelDb) {
        updateClientInAppointmentsUseCase.execute(clientModelDb)
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

    fun updateUserData(event: String) {
        updateUserDataUseCase.execute(event)
    }
}