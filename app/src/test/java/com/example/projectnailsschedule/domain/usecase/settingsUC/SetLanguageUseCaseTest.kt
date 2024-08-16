package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class SetLanguageUseCaseTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should set language in Settings repository and return true`() {
        val setLanguageUseCase = SetLanguageUseCase(settingsRepository = settingsRepository)
        val expected = true
        val language = "en"

        val actual = setLanguageUseCase.execute(language)

        Assertions.assertEquals(expected, actual)
    }
}