package com.example.projectnailsschedule.util

import android.os.Environment
import android.util.Log
import java.io.File

/**
 * Класс создает необходимые папки
 * */

class WorkFolders{

    private val LOG = this::class.simpleName
    private val mainFolderName: String = "ClientSchedule"
    private val folderPath = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        mainFolderName
    )

    fun run() {
        if (!directoryExists()) {
            createDbDir()
        }
    }

    private fun createDbDir() {
        // Создаем папку, для хранения файлов приложения
        try {
            if (folderPath.mkdirs()) {
                Log.e(LOG, "Directory $mainFolderName created")
            }
        } catch (e: Exception) {
            Log.e(LOG, "Directory creation error!")
            Log.e(LOG, e.toString())
        }
    }

    private fun directoryExists(): Boolean {
        // Проверяем, создана ли папка для хранения файлов приложения
        return if (folderPath.exists()) {
            Log.e(LOG, "Folder $mainFolderName exists")
            true
        } else {
            Log.e(LOG, "Folder $mainFolderName is not exists")
            false
        }
    }

    fun getFolderPath(): File {
        return folderPath
    }
}