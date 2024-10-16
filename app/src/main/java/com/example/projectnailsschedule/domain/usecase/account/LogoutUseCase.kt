package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context

class LogoutUseCase(private val context: Context) {
    suspend fun execute(): Boolean {
        return true
    }
}