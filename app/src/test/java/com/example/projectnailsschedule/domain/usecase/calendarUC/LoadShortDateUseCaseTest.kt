package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class LoadShortDateUseCaseTest {
    private val scheduleRepository = mock<ScheduleRepository>()

    @Test
    fun `should return date appointments from Schedule repository`() {
        val loadShortDateUseCase = LoadShortDateUseCase(scheduleRepository = scheduleRepository)
        val testDateParams = DateParams()
        val expected = arrayOf(
            AppointmentModelDb(deleted = false),
            AppointmentModelDb(deleted = false)
        )

        Mockito.`when`(scheduleRepository.getDateAppointments(dateParams = testDateParams)).thenReturn(expected)

        val actual = loadShortDateUseCase.execute(selectedDateParams = testDateParams)

        Assertions.assertArrayEquals(expected, actual)
    }
}
