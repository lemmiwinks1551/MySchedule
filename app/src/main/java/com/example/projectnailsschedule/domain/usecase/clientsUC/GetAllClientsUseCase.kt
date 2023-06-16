package com.example.projectnailsschedule.domain.usecase.clientsUC

import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class GetAllClientsUseCase(private val clientsRepository: ClientsRepository) :
    ViewModelProvider.Factory {

}