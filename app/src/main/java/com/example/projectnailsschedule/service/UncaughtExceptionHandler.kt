package com.example.projectnailsschedule.service

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.projectnailsschedule.R
import java.io.File


class UncaughtExceptionHandler : Thread.UncaughtExceptionHandler {

    private val filePath = File(String.format("${WorkFolders().getFolderPath()}/log.log"))
    private val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
    lateinit var context: Context
    private val LOG = this::class.simpleName


    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            // Write log file
            writeLogFile()

            // SendLogFile
            sendLogFile()
        } catch (e: Exception) {
            Log.e(LOG, e.toString())
        } finally {
            // Call standard handler
            oldHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun writeLogFile() {
        // Create log file, if not exists
        if (filePath.exists()) {
            filePath.delete()
        }

        // Write log file
        val cmd = "logcat -d -f" + filePath.absolutePath
        filePath.createNewFile()
        Runtime.getRuntime().exec(cmd)
        Log.e(LOG, "Log file created")
    }

    private fun sendLogFile() {
        // Send Log file by email
        val supportEmailSubject = context.getString(R.string.support_subject)
        val supportEmailAddress = context.getString(R.string.support_email_uri)
        val supportEmailAttachment = Uri.fromFile(filePath)

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(supportEmailAddress) // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, supportEmailSubject)
            putExtra(Intent.EXTRA_STREAM, supportEmailAttachment)
        }
        try {
            startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            Log.e(LOG, e.toString())
        }
    }
}