package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class SetLightThemeUseCaseTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should set light theme in Settings repository and return true`() {
        val setLightThemeUseCase = SetLightThemeUseCase(settingsRepository = settingsRepository)
        val expected = true
        val actual = setLightThemeUseCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}