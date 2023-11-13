package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetAllAppointmentsUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    fun `should return all appointments from Schedule repository`() {
        val testAppointments0 = AppointmentModelDb(deleted = false)
        val testAppointments1 = AppointmentModelDb(deleted = false)
        val testList = listOf(testAppointments0, testAppointments1)

        Mockito.`when`(scheduleRepository.getAllAppointments()).thenReturn(testList)

        val getAllAppointmentsUseCaseTest = GetAllAppointmentsUseCase(scheduleRepository = scheduleRepository)
        val actual = getAllAppointmentsUseCaseTest.execute()
        val expected = listOf(testAppointments0, testAppointments1)

        Assertions.assertEquals(actual, expected)
    }
}