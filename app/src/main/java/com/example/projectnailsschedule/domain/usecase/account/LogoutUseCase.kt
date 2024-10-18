package com.example.projectnailsschedule.domain.usecase.account

import android.content.Context
import com.example.projectnailsschedule.domain.repository.api.LoginApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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




            true
        } finally {
            // Сброс состояния после завершения запроса
            isExecuting = false
        }
    }
}
