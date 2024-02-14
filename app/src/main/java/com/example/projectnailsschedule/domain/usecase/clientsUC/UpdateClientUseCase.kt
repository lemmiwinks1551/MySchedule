package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class UpdateClientUseCase(private val clientsRepository: ClientsRepository) {

    suspend fun execute(clientModelDb: ClientModelDb):Boolean {
        clientsRepository.updateClient(clientModelDb)
        return true
    }
}