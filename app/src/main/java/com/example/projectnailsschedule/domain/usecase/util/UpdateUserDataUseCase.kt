package com.example.projectnailsschedule.domain.usecase.util

import com.example.projectnailsschedule.domain.models.UserDataManager

class UpdateUserDataUseCase {
    fun execute(event: String) {
        UserDataManager.updateUserData(event)
    }
}