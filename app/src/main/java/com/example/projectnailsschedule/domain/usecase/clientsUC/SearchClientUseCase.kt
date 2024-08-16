package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository

class SearchClientUseCase(private val clientsRepository: ClientsRepository) {

    suspend fun execute(searchQuery: String): MutableList<ClientModelDb> {
        return clientsRepository.searchClient(searchQuery)
    }
}