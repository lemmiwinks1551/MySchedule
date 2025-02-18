package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.projectnailsschedule.domain.models.UserDataManager

class AppLifecycleObserver : DefaultLifecycleObserver {

    private val userDataManager = UserDataManager

    override fun onCreate(owner: LifecycleOwner) {
        userDataManager.updateUserData(event = "Main Activity onCreate")
        super.onCreate(owner)
    }

/*    override fun onDestroy(owner: LifecycleOwner) {
        userDataManager.updateUserData("Main Activity onDestroy")
        super.onDestroy(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        userDataManager.updateUserData("Main Activity onPause")
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        userDataManager.updateUserData("Main Activity onResume")
        super.onResume(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        userDataManager.updateUserData("Main Activity onStart")
        super.onStart(owner)
    }*/

    override fun onStop(owner: LifecycleOwner) {
        userDataManager.updateUserData(event = "Main Activity onStop")
        super.onStop(owner)
    }
}

