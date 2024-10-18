package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context
import kotlinx.coroutines.delay

class LogoutUseCase(private val context: Context) {

    @Volatile
    private var isExecuting = false

    suspend fun execute(): Boolean? {
        if (isExecuting) {
            // Если запрос уже выполняется, игнорируем новые вызовы
            return null
        }

        isExecuting = true

        return try {
            // Выполнение запроса
            delay(3000)
            true
        } finally {
            // Сброс состояния после завершения запроса
            isExecuting = false
        }
    }
}
