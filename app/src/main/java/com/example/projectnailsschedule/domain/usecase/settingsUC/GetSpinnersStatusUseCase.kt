package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class GetSpinnersStatusUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(): Boolean {
        return settingsRepository.getSpinnerStatus()
    }
}