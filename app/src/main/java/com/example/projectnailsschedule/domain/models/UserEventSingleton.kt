package com.example.projectnailsschedule.domain.models

// Класс UserEventSingleton для хранения данных
data class UserEventSingleton(
    var sessionId: String = "",
    var model: String = "",
    var device: String = "",
    var dateTime: String = "",
    var appVersionName: String = "",
    var event: String = ""
)

// Синглтон для управления состоянием UserEventSingleton
object UserEventManager {
    // Экземпляр UserEventSingleton
    private val userEvent = UserEventSingleton()

    // Получение текущего состояния
    fun getUserEvent(): UserEventSingleton = userEvent

    // Обновление данных
    fun updateUserEvent(
        userId: String? = null,
        model: String? = null,
        device: String? = null,
        dateTime: String? = null,
        appVersionName: String? = null,
        event: String? = null
    ) {
        userId?.let { userEvent.sessionId = it }
        model?.let { userEvent.model = it }
        device?.let { userEvent.device = it }
        dateTime?.let { userEvent.dateTime = it }
        appVersionName?.let { userEvent.appVersionName = it }
        event?.let { userEvent.event = it }
    }
}


