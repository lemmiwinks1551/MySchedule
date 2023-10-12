package com.example.projectnailsschedule.domain.usecase.importExportUc

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ExportUseCase(val context: Context) {

    fun execute(treeUri: Uri) {
        val folder = DocumentFile.fromTreeUri(context, treeUri)
        if (folder != null && folder.exists() && folder.isDirectory) {
            val sourceDir = File(
                context.applicationInfo.dataDir,
                "databases"
            )
            val files = sourceDir.listFiles()

            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        val targetFile = folder.createFile("*/*", file.name)
                        if (targetFile != null) {
                            val targetUri = targetFile.uri
                            val outputStream =
                                context.contentResolver.openOutputStream(targetUri)
                            if (outputStream != null) {
                                try {
                                    val buffer = ByteArray(1024)
                                    var length: Int
                                    val sourceStream = FileInputStream(file)
                                    while (sourceStream.read(buffer).also { length = it } > 0) {
                                        outputStream.write(buffer, 0, length)
                                    }
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    Toast.makeText(
                                        context,
                                        "Error exporting ${file.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } finally {
                                    outputStream.close()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}