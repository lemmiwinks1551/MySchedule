package com.example.projectnailsschedule.presentation.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class NetworkObserver(
    context: Context,
    private val onNetworkStatusChanged: (Boolean) -> Unit // Pass true for connected, false for disconnected
) : DefaultLifecycleObserver {

    private val log = this::class.simpleName
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.e(log, "Есть интернет")
            onNetworkStatusChanged(true)  // Network is available
        }

        override fun onLost(network: Network) {
            Log.e(log, "Нет интернета")
            onNetworkStatusChanged(false) // Network is lost
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        registerNetworkCallback()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        unregisterNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}