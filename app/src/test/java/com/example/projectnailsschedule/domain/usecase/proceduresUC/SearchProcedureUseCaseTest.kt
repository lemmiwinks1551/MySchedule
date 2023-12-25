package com.example.projectnailsschedule.domain.usecase.proceduresUC

import androidx.lifecycle.MutableLiveData
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class SearchProcedureUseCaseTest {
    private val procedureRepository = mock<ProcedureRepository>()

    @Test
    fun `should get liveData list from Clients repository`() {
        val searchProcedureUseCase =
            SearchProcedureUseCase(procedureRepository = procedureRepository)
        val testLiveData = MutableLiveData<List<ProcedureModelDb>>()
        val query = "query"

        Mockito.`when`(searchProcedureUseCase.execute(query)).thenReturn(testLiveData)

        val actual = searchProcedureUseCase.execute(query)
        val expected = testLiveData

        Assertions.assertEquals(expected, actual)
    }
}