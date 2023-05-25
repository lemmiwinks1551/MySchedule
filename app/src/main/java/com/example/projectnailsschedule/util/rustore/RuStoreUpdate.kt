package com.example.projectnailsschedule.util.rustore

import android.content.Context
import android.util.Log
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateInfo
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.InstallStatus

class RuStoreUpdate(val context: Context) {
    val log = "RuStoreUpdate"

    fun checkForUpdates() {
        val updateManager = RuStoreAppUpdateManagerFactory.create(context)
        var appUpdateInfo: AppUpdateInfo?

        updateManager
            .getAppUpdateInfo()
            .addOnSuccessListener { info ->
                appUpdateInfo = info
                Log.e(log, appUpdateInfo!!.updateAvailability.toString())
                updateManager
                    .startUpdateFlow(appUpdateInfo!!, AppUpdateOptions.Builder().build())
                    .addOnSuccessListener { resultCode ->
                        Log.e(log, resultCode.toString())
                    }
                    .addOnFailureListener { throwable ->
                        Log.e(log, throwable.toString())
                    }
            }
            .addOnFailureListener { throwable ->
                Log.e(log, throwable.message!!)
            }
        updateManager.registerListener { state ->
            if (state.installStatus == InstallStatus.DOWNLOADED) {
                Log.e(log, "Update is ready to install")
                updateManager
                    .completeUpdate()
                    .addOnFailureListener { throwable ->
                        Log.e(log, throwable.message!!)
                    }
            }
        }
    }
}