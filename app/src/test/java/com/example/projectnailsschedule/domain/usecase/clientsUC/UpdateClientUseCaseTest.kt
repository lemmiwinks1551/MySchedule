package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class UpdateClientUseCaseTest {
    private val clientsRepository = mock<ClientsRepository>()

    @Test
    fun `should update Client in Clients repository and return true`() {
        val updateClientUseCase = UpdateClientUseCase(clientsRepository = clientsRepository)
        val testClient = ClientModelDb()

        val expected = true
        val actual = updateClientUseCase.execute(clientModelDb = testClient)

        Assertions.assertEquals(expected, actual)
    }
}