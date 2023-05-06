package com.example.projectnailsschedule.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.projectnailsschedule.R
import java.io.File


class UncaughtExceptionHandler : Thread.UncaughtExceptionHandler {

    private val filePath = File(String.format("${WorkFolders().getFolderPath()}/log.log"))
    private val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
    lateinit var context: Context
    private val log = this::class.simpleName


    override fun uncaughtException(thread: Thread, throwable: Throwable) {

    }
}