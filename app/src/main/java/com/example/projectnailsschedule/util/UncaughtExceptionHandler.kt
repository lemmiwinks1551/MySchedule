package com.example.projectnailsschedule.util

import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UncaughtExceptionHandler(
    private val sendUserDataUseCase: SendUserDataUseCase
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val userData = UserDataManager.getUserData()
        userData.event = throwable.message.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                sendUserDataUseCase.execute(userData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
