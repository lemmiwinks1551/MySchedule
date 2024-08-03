package com.example.projectnailsschedule.domain.models

import android.os.Build
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.sync.Mutex
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.LinkedList

data class UserData(
    var sessionId: String = Util().generateUniqueId(),
    var model: String = Build.MODEL,
    var device: String = Build.DEVICE,
    var dateTime: String = "",
    var appVersionName: String = BuildConfig.VERSION_NAME,
    var event: String = ""
)

var userDateQueue = LinkedList<UserData>()

object UserDataManager {
    private val userEvent = UserData()

    fun getUserData(): UserData {
        return userEvent.copy()
    }

    fun updateUserData(
        dateTime: String? = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
        event: String? = null
    ) {
        dateTime?.let { userEvent.dateTime = it }
        event?.let { userEvent.event = it }
    }

    fun updateQueue() {
        userDateQueue.add(this.getUserData())
    }
}


