package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class GetThemeUseCaseTest {
    private val settingsRepository = mock<SettingsRepository>()

    @Test
    fun `get Theme as boolean from Settings repository`() {
        val getThemeUseCase = GetThemeUseCase(settingsRepository = settingsRepository)
        val expected = true

        Mockito.`when`(settingsRepository.loadTheme()).thenReturn(expected)
        val actual = getThemeUseCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}