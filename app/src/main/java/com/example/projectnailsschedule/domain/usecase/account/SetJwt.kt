package com.example.projectnailsschedule.domain.usecase.account

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class SetJwt(private val settingsRepository: SettingsRepository) {

    fun execute(jwt: String?): Boolean {
        return settingsRepository.setJwt(jwt)
    }
}