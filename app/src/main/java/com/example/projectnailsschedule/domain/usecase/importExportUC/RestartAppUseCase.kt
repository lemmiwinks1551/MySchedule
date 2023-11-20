package com.example.projectnailsschedule.domain.usecase.importExportUC

import android.app.Activity
import android.content.Context
import android.content.Intent

class RestartAppUseCase(val context: Context) {

    fun execute() {
        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

        if (context is Activity) {
            context.finish()
        }
    }
}