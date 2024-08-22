package com.example.projectnailsschedule.domain.usecase.clientsUC

import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class InsertClientUseCaseTest {
    private val clientsRepository = mock<ClientsRepository>()

    @Test
    suspend fun `should insert client into Clients repository and return true`() {
        val insertClientUseCase = InsertClientUseCase(clientsRepository = clientsRepository)
        val testClient = ClientModelDb()
        val expected = true
        val actual = insertClientUseCase.execute(testClient)

        Assertions.assertEquals(expected, actual)
    }
}