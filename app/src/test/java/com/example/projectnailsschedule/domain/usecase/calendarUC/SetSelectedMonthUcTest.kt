package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.time.LocalDate

internal class SetSelectedMonthUcTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should set selected date into settings repository and return true`() {
        val useCase = SetSelectedMonthUc(settingsRepository = settingsRepository)
        val testDate = LocalDate.now()
        val expected = true
        val actual = useCase.execute(testDate)

        Assertions.assertEquals(expected, actual)
    }
}