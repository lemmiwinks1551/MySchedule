package com.example.projectnailsschedule.domain.usecase.dateUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class DeleteAppointmentUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    fun `should delete Appointment from Schedule repository and return true`() {
        val deleteAppointmentUseCase = DeleteAppointmentUseCase(scheduleRepository = scheduleRepository)
        val testAppointment = AppointmentModelDb(deleted = false)
        val expected = true
        val actual = deleteAppointmentUseCase.execute(testAppointment)

        Assertions.assertEquals(expected, actual)
    }
}