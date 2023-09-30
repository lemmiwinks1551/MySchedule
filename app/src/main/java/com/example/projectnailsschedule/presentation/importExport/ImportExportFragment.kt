package com.example.projectnailsschedule.presentation.importExport

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentImportExportBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ImportExportFragment : Fragment() {

    private var _binding: FragmentImportExportBinding? = null
    private var settingsViewModel: ImportExportViewModel? = null

    private val binding get() = _binding!!

    private val PERMISSION_REQUEST_CODE = 999
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // create ViewModel object with Factory
        settingsViewModel = ViewModelProvider(
            this,
            ImportExportViewModelFactory(this.context)
        )[ImportExportViewModel::class.java]

        _binding = FragmentImportExportBinding.inflate(inflater, container, false)

        // init all widgets
        initAllWidgets()

        // init click listeners
        initClickListeners()

        return binding.root
    }

    private fun initAllWidgets() {
        binding.exportButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Разрешение предоставлено, выполните необходимые действия
                    exportDatabaseFiles()
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.exported),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Запрос разрешения для управления всеми файлами
                    val uri = Uri.parse("package:${requireContext().packageName}")
                    startActivity(
                        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    )
                }
            } else {
                // Ниже Android 11 (Android 10 и более ранние версии)
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }

        binding.importButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Разрешение предоставлено, выполните необходимые действия
                    importDatabaseFiles()
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.imported),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Запрос разрешения для управления всеми файлами
                    val uri = Uri.parse("package:${requireContext().packageName}")
                    startActivity(
                        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    )
                }
            } else {
                // Ниже Android 11 (Android 10 и более ранние версии)
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun initClickListeners() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение предоставлено, выполните операции экспорта
                    exportDatabaseFiles()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.permission_warning),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun exportDatabaseFiles() {
        // Предположим, что ваши базы данных хранятся в папке databases
        val sourceDir = File(requireContext().applicationInfo.dataDir, "databases")
        val destinationDir =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/MySchedule/"

        // Создаем целевую папку, если она не существует
        val destinationFolder = File(destinationDir)
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs()
        }

        // Получаем список файлов с расширениями .db, .db-shm и .db-wal
        val dbFiles = sourceDir.listFiles { _, name ->
            name.endsWith(".db") || name.endsWith(".db-shm") || name.endsWith(".db-wal")
        }


        // Проверяем, что список файлов не пустой
        if (dbFiles != null) {
            for (dbFile in dbFiles) {
                val sourcePath = dbFile.path
                val destinationPath = "$destinationDir/${dbFile.name}"

                // Выполняем копирование файла
                copyFile(sourcePath, destinationPath)
            }
        }
    }

    private fun copyFile(sourcePath: String, destinationPath: String) {
        val sourceFile = File(sourcePath)
        val destinationFile = File(destinationPath)

        val inputStream = FileInputStream(sourceFile)
        val outputStream = FileOutputStream(destinationFile)
        try {
            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
    }

    private fun importDatabaseFiles() {
        // Путь к папке MySchedule
        val sourceDir =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/MySchedule/"

        // Путь к папке баз данных приложения
        val destinationDir = File(requireContext().applicationInfo.dataDir, "databases")
        clearDirectory(destinationDir)

        // Создаем целевую папку, если она не существует
        val destinationFolder = File(destinationDir.path)
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs()
        }

        // Получаем список файлов в папке MySchedule
        val filesInMySchedule = File(sourceDir).listFiles()

        if (filesInMySchedule != null) {
            for (file in filesInMySchedule) {
                val sourcePath = file.path
                val destinationPath = "${destinationDir.path}/${file.name}"

                // Выполняем копирование файла
                copyFile(sourcePath, destinationPath)
            }
        }
    }

    private fun clearDirectory(dir: File) {
        val files = dir.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    clearDirectory(file) // Рекурсивно очищаем поддиректории
                } else {
                    file.delete() // Удаляем файл
                }
            }
        }
    }
}