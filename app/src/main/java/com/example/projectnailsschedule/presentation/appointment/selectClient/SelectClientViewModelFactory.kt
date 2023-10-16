package com.example.projectnailsschedule.presentation.appointment.selectClient

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ClientRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*

class SelectClientViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val clientsRepositoryImpl = ClientRepositoryImpl(context = context!!)

    private var searchClientUseCase = SearchClientUseCase(clientsRepository = clientsRepositoryImpl)

    private var startVkUc = StartVkUc(context!!)
    private var startTelegramUc = StartTelegramUc(context!!)
    private var startInstagramUc = StartInstagramUc(context!!)
    private var startWhatsAppUc = StartWhatsAppUc(context!!)
    private var startPhoneUc = StartPhoneUc(context!!)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectClientViewModel(
            searchClientUseCase = searchClientUseCase,
            startVkUc = startVkUc,
            startTelegramUc = startTelegramUc,
            startInstagramUc = startInstagramUc,
            startWhatsAppUc = startWhatsAppUc,
            startPhoneUc = startPhoneUc
        ) as T
    }
}