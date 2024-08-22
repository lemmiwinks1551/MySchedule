package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class SetDarkThemeUseCaseTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should set dark theme into Settings repository and return true`() {
        val setDarkThemeUseCase = SetDarkThemeUseCase(settingsRepository = settingsRepository)
        val expected = true
        val actual = setDarkThemeUseCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}