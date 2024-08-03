package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(val mainViewModel: MainViewModel) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        mainViewModel.addUserData("onCreate")
        super.onCreate(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mainViewModel.addUserData("onDestroy")
        super.onDestroy(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        mainViewModel.addUserData("onPause")
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        mainViewModel.addUserData("onResume")
        super.onResume(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        mainViewModel.addUserData("onStart")
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        mainViewModel.addUserData("onStop")
        super.onStop(owner)
    }
}

