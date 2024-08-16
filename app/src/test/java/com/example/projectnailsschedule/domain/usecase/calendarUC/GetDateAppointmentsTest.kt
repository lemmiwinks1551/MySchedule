package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import java.time.LocalDate

internal class GetDateAppointmentsTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    suspend fun `should return date appointments from Schedule repository`() {
        val getDateAppointments = GetDateAppointments(scheduleRepository = scheduleRepository)
        val testLocalDate = LocalDate.now()
        val expected = mutableListOf(
            AppointmentModelDb(deleted = false),
            AppointmentModelDb(deleted = false)
        )

        Mockito.`when`(scheduleRepository.getDateAppointments(date = testLocalDate)).thenReturn(expected)

        val actual = getDateAppointments.execute(date = testLocalDate)

        Assertions.assertArrayEquals(expected as Array<*>, actual as Array<*>)
    }
}
