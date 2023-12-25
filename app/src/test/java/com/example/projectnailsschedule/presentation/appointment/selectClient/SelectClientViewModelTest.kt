package com.example.projectnailsschedule.presentation.appointment.selectClient

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.MutableLiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class SelectClientViewModelTest {

    lateinit var selectClientViewModel: SelectClientViewModel

    private val searchClientUseCase = mock<SearchClientUseCase>()
    private val startVkUc = mock<StartVkUc>()
    private val startTelegramUc = mock<StartTelegramUc>()
    private val startInstagramUc = mock<StartInstagramUc>()
    private val startWhatsAppUc = mock<StartWhatsAppUc>()
    private val startPhoneUc = mock<StartPhoneUc>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(searchClientUseCase)
        Mockito.reset(startVkUc)
        Mockito.reset(startTelegramUc)
        Mockito.reset(startInstagramUc)
        Mockito.reset(startWhatsAppUc)
        Mockito.reset(startPhoneUc)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @BeforeEach
    fun beforeEach() {
        selectClientViewModel = SelectClientViewModel(
            searchClientUseCase,
            startVkUc,
            startTelegramUc,
            startInstagramUc,
            startWhatsAppUc,
            startPhoneUc
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
    fun `should search client and return liveData list`() {
        val testLiveData = MutableLiveData<List<ClientModelDb>>()
        val query = "query"

        Mockito.`when`(searchClientUseCase.execute(query)).thenReturn(testLiveData)

        val actual = selectClientViewModel.searchClients(query)
        val expected = testLiveData

        Assertions.assertEquals(expected, actual)
    }
}