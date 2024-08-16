package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository

class GetClientByIdUseCase(private val clientsRepository: ClientsRepository) {

    suspend fun execute(id: Long): ClientModelDb {
        return clientsRepository.getClientById(id = id)
    }
}