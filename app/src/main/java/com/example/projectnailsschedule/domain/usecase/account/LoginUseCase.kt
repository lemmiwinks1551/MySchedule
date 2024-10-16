package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context

class LoginUseCase(private val context: Context) {

    suspend fun execute(login: String, password: String): String {
        return "jwt"
    }
}