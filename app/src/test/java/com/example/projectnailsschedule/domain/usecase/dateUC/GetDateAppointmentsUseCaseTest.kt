package com.example.projectnailsschedule.domain.usecase.dateUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class GetDateAppointmentsUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    fun `should return array of Appointments from Schedule repository`() {
        val getDateAppointmentsUseCase = GetDateAppointmentsUseCase(scheduleRepository = scheduleRepository)
        val testDateParams = DateParams()
        val testAppointment1 = AppointmentModelDb(deleted = false)
        val testAppointment2 = AppointmentModelDb(deleted = false)
        val expectedAppointments = arrayOf(testAppointment1, testAppointment2)

        Mockito.`when`(scheduleRepository.getDateAppointments(testDateParams)).thenReturn(expectedAppointments)

        val actual = getDateAppointmentsUseCase.execute(testDateParams)

        Assertions.assertEquals(expectedAppointments, actual)
    }
}