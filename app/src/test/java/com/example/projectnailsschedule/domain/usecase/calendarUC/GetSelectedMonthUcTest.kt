package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import java.time.LocalDate

internal class GetSelectedMonthUcTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should return selected month from Settings repository`() {
        val getSelectedMonthUseCase = GetSelectedMonthUc(settingsRepository = settingsRepository)
        val expected = LocalDate.now()

        Mockito.`when`(settingsRepository.getSelectedMonth()).thenReturn(expected)

        val actual = getSelectedMonthUseCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}