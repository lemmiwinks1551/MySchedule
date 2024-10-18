package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context
import kotlinx.coroutines.delay

class LoginUseCase(private val context: Context) {

    @Volatile
    private var isExecuting = false

    suspend fun execute(login: String, password: String): String? {
        if (isExecuting) {
            // Если запрос уже выполняется, игнорируем новые нажатия
            return null
        }

        isExecuting = true
        return try {
            // Выполнение запроса
            delay(3000)
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraXJpbGwiLCJpYXQiOjE3Mjg1ODI4MjUsImV4cCI6MTcyOTE4NzYyNX0._PckEusnht9D4-xM4QC_TH4dmE99YUX3jjXjsIqeFrf0DsDEsgumaD7qysDiFOGrxrDHDLiQKEVqKfdjcNBa6g"
        } finally {
            // Сброс состояния после завершения запроса
            isExecuting = false
        }
    }
}