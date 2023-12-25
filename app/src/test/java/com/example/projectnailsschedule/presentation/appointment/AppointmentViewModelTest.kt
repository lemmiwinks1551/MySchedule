package com.example.projectnailsschedule.presentation.appointment

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class AppointmentViewModelTest {

    lateinit var appointmentViewModel: AppointmentViewModel

    private val insertAppointmentUseCase = mock<InsertAppointmentUseCase>()
    private val updateAppointmentUseCase = mock<UpdateAppointmentUseCase>()
    private val startVkUc = mock<StartVkUc>()
    private val startTelegramUc = mock<StartTelegramUc>()
    private val startInstagramUc = mock<StartInstagramUc>()
    private val startWhatsAppUc = mock<StartWhatsAppUc>()
    private val startPhoneUc = mock<StartPhoneUc>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(insertAppointmentUseCase)
        Mockito.reset(updateAppointmentUseCase)
        Mockito.reset(startVkUc)
        Mockito.reset(startTelegramUc)
        Mockito.reset(startInstagramUc)
        Mockito.reset(startWhatsAppUc)
        Mockito.reset(startPhoneUc)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @BeforeEach
    fun beforeEach() {
        appointmentViewModel = AppointmentViewModel(
            insertAppointmentUseCase,
            updateAppointmentUseCase,
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
    fun `should insert appointment`() {
        val testAppointment = AppointmentModelDb(deleted = false)

        Mockito.`when`(insertAppointmentUseCase.execute(testAppointment)).thenReturn(true)

        val expected = true
        val actual = appointmentViewModel.insertAppointment(testAppointment)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should update appointment`() {
        val testAppointment = AppointmentModelDb(deleted = false)

        Mockito.`when`(updateAppointmentUseCase.execute(testAppointment)).thenReturn(true)

        val expected = true
        val actual = appointmentViewModel.updateAppointment(testAppointment)

        Assertions.assertEquals(expected, actual)
    }
}