package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context

class SendPasswordResetConfirmation(private val context: Context) {
    suspend fun execute(): Boolean {
        return true
    }
}