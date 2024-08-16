package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class DeleteClientUseCaseTest {
    private val clientsRepository = mock<ClientsRepository>()

    @Test
    suspend fun `should delete Client from Clients repository and return true`() {
        val deleteClientUseCase = DeleteClientUseCase(clientsRepository = clientsRepository)
        val testClient = ClientModelDb()
        val expected = true
        val actual = deleteClientUseCase.execute(testClient)

        Assertions.assertEquals(expected, actual)
    }
}