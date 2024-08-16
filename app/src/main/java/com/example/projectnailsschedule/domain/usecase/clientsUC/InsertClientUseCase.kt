package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository

class InsertClientUseCase(private val clientsRepository: ClientsRepository) {

    suspend fun execute(clientModelDb: ClientModelDb): Long {
        return clientsRepository.insertClient(clientModelDb)
    }
}