package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.repo.ProcedureRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class UpdateProcedureUseCaseTest {
    private val procedureRepository = mock<ProcedureRepository>()

    @Test
    fun `should update Procedure in Procedure repository and return true`() {
        val updateProcedureUseCase =
            UpdateProcedureUseCase(procedureRepository = procedureRepository)
        val procedureTest = ProcedureModelDb()
        val expected = true
        runBlocking {
            val actual = updateProcedureUseCase.execute(procedureTest)
            Assertions.assertEquals(expected, actual)
        }
    }
}