package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class UpdateAppointmentUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    fun `should update appointment in Schedule repository and return true`() {
        val updateAppointmentUseCase = UpdateAppointmentUseCase(scheduleRepository = scheduleRepository)
        val testAppointment = AppointmentModelDb(_id = null, deleted = false)

        val expected = true
        val actual = updateAppointmentUseCase.execute(testAppointment)

        Assertions.assertEquals(expected, actual)
    }
}