package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class InsertAppointmentUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    suspend fun `should insert appointment into Schedule repository`() {
        val insertAppointmentUseCase =
            InsertAppointmentUseCase(scheduleRepository = scheduleRepository)
        val testAppointment = AppointmentModelDb(_id = null, deleted = false)

        val actual = insertAppointmentUseCase.execute(testAppointment)
        val expected = true

        Assertions.assertEquals(expected, actual)
    }
}