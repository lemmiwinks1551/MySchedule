package com.example.projectnailsschedule.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.projectnailsschedule.R
import java.io.File


class LogFile: Thread.UncaughtExceptionHandler {

    private val filePath = File(String.format("${WorkFolders().getFolderPath()}/log.log"))
    private val LOG = this::class.simpleName

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        TODO("Not yet implemented")
    }

    fun writeLogFile() {
        // Write log file
        if (filePath.exists()) {
            filePath.delete()
        }
        val cmd = "logcat -d -f" + filePath.absolutePath
        filePath.createNewFile()
        Runtime.getRuntime().exec(cmd)
        Log.e(LOG, "Log file created")
    }

    fun sendLogFile(context: Context) {
        // Send Log file to email
        val intentMail = Intent(Intent.ACTION_SEND)
        val supportEmail = context.getString(R.string.support_email)
        val filePathUri = Uri.fromFile(filePath)

        intentMail.type = "message/rfc822"
        with(intentMail) {
            putExtra(Intent.EXTRA_EMAIL, supportEmail)
            putExtra(Intent.EXTRA_STREAM, filePathUri)
            putExtra(Intent.EXTRA_SUBJECT, "Мое расписание, логи")
        }

        startActivity(context, intentMail, null)
    }
}