package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.repo.ProcedureRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class DeleteProcedureUseCaseTest {
    private val procedureRepository = mock<ProcedureRepository>()

    @Test
    fun `should delete Procedure from Procedure repository and return true`() {
        val deleteProcedureUseCase =
            DeleteProcedureUseCase(procedureRepository = procedureRepository)
        val testProcedure = ProcedureModelDb()
        val expected = true
        runBlocking {
            val actual = deleteProcedureUseCase.execute(testProcedure)
            Assertions.assertEquals(expected, actual)
        }
    }
}