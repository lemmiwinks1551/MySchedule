package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class UpdateClientUseCase(private val clientsRepository: ClientsRepository) {

    fun execute(clientModelDb: ClientModelDb) {
        clientsRepository.updateClient(clientModelDb)
    }
}