package com.example.projectnailsschedule.domain.usecase.account

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class GetJwt(private val settingsRepository: SettingsRepository) {

    suspend fun execute(): String? {
        return settingsRepository.getJwt()
    }
}