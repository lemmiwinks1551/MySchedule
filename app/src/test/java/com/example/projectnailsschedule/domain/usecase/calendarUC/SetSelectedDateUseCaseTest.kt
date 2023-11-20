package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import java.time.LocalDate

internal class SetSelectedDateUseCaseTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should set selected date in Settings repository`() {
        val setSelectedDateUseCase = SetSelectedDateUseCase(settingsRepository = settingsRepository)
        val testSelectedDate = LocalDate.now()
        val expected = true

        val actual = setSelectedDateUseCase.execute(selectedDate = testSelectedDate)

        Assertions.assertEquals(expected, actual)

    }
}
