package com.example.projectnailsschedule.presentation.appointment.selectProcedure

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.MutableLiveData
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class SelectProcedureViewModelTest {

    private lateinit var selectProcedureViewModel: SelectProcedureViewModel

    private val searchProcedureUseCase = mock<SearchProcedureUseCase>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(searchProcedureUseCase)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @BeforeEach
    fun beforeEach() {
        selectProcedureViewModel = SelectProcedureViewModel(
            searchProcedureUseCase
        )

        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

        })
    }

    @Test
    fun `should search procedure and return liveData list`() {
        val testLiveData = MutableLiveData<List<ProcedureModelDb>>()
        val query = "query"

        Mockito.`when`(searchProcedureUseCase.execute(query)).thenReturn(testLiveData)

        val actual = selectProcedureViewModel.searchProcedures(query)
        val expected = testLiveData

        Assertions.assertEquals(expected, actual)
    }
}