package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class GetAllAppointmentsUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    fun `should return all appointments from Schedule repository`() {
        val testAppointment0 = AppointmentModelDb(deleted = false)
        val testAppointment1 = AppointmentModelDb(deleted = false)
        val testList = listOf(testAppointment0, testAppointment1)

        Mockito.`when`(scheduleRepository.getAllAppointments()).thenReturn(testList)

        val getAllAppointmentsUseCase = GetAllAppointmentsUseCase(scheduleRepository = scheduleRepository)
        val actual = getAllAppointmentsUseCase.execute()
        val expected = listOf(testAppointment0, testAppointment1)

        Assertions.assertEquals(actual, expected)
    }
}