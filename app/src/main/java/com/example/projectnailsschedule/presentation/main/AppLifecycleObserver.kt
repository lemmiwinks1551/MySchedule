package com.example.projectnailsschedule.presentation.main

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.projectnailsschedule.domain.models.UserEventManager
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor(
    private val sendUserDataUseCase: SendUserDataUseCase
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        sendUserData("onCreate")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        sendUserData("onDestroy")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        sendUserData("onPause")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        sendUserData("onResume")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        sendUserData("onStart")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        sendUserData("onStop")
    }

    private fun sendUserData(event: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val done = async {
                UserEventManager.updateUserEvent(event = event)
            }.await()
            if (done) {
                Log.i("AppLifecycleObserver", event)
                sendUserDataUseCase.execute()
            }
        }
    }
}

