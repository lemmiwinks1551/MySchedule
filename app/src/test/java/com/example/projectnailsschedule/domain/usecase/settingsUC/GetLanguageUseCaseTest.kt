package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class GetLanguageUseCaseTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `should return Language from Settings repository and return String`() {
        val getLanguageUseCase = GetLanguageUseCase(settingsRepository = settingsRepository)
        val expected = "English"

        Mockito.`when`(settingsRepository.getLanguage()).thenReturn(expected)
        val actual = getLanguageUseCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}