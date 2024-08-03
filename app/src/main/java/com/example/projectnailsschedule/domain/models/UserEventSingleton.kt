package com.example.projectnailsschedule.domain.models

import android.os.Build
import android.util.Log
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class UserEventSingleton(
    var sessionId: String = Util().generateUniqueId(),
    var model: String = Build.MODEL,
    var device: String = Build.DEVICE,
    var dateTime: String = "",
    var appVersionName: String = BuildConfig.VERSION_NAME,
    var event: String = ""
)

object UserEventManager {
    private val userEvent = UserEventSingleton()
    private val mutex = Mutex() // Mutex для синхронизации

    suspend fun getUserEvent(): UserEventSingleton = mutex.withLock {
        userEvent.copy()
    }

    suspend fun updateUserEvent(
        dateTime: String? = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
        event: String? = null
    ): Boolean {
        return mutex.withLock {
            dateTime?.let { userEvent.dateTime = it }
            event?.let { userEvent.event = it }
            Log.i("AppLifecycleObserverUpdater", userEvent.event)
            true
        }
    }
}


