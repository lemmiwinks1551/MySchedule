package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class InsertProcedureUseCaseTest {
    private val procedureRepository = mock<ProcedureRepository>()

    @Test
    fun `should insert Procedure to the Procedure repository and return true`() {
        val insertProcedureUseCase =
            InsertProcedureUseCase(procedureRepository = procedureRepository)
        val procedureTest = ProcedureModelDb()
        val expected = true
        runBlocking {
            val actual = insertProcedureUseCase.execute(procedureTest)
            Assertions.assertEquals(expected, actual)
        }
    }
}