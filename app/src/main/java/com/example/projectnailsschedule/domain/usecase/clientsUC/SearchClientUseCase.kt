package com.example.projectnailsschedule.domain.usecase.clientsUC

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class SearchClientUseCase(private val clientsRepository: ClientsRepository) {

    suspend fun execute(searchQuery: String): LiveData<List<ClientModelDb>> {
        return clientsRepository.searchClient(searchQuery)
    }
}