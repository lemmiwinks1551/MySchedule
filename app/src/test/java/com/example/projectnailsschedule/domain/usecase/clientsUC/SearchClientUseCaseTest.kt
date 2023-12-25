package com.example.projectnailsschedule.domain.usecase.clientsUC

import androidx.lifecycle.MutableLiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class SearchClientUseCaseTest {
    private val clientsRepository = mock<ClientsRepository>()

    @Test
    fun `should get liveData list from Clients repository`() {
        val searchClientUseCase = SearchClientUseCase(clientsRepository = clientsRepository)
        val testLiveData = MutableLiveData<List<ClientModelDb>>()
        val query = "query"

        Mockito.`when`(searchClientUseCase.execute(query)).thenReturn(testLiveData)

        val actual = searchClientUseCase.execute(query)
        val expected = testLiveData

        Assertions.assertEquals(expected, actual)
    }
}