package com.example.projectnailsschedule.presentation.importExport

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentImportExportBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ImportExportFragment : Fragment() {

    private val OPEN_DIRECTORY_REQUEST_CODE = 123
    private val EXPORT_REQUEST_CODE = 456
    private val IMPORT_REQUEST_CODE = 789
    private lateinit var exportButton: Button
    private lateinit var importButton: Button

    private var _binding: FragmentImportExportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportExportBinding.inflate(inflater, container, false)

        importButton = binding.importButton
        exportButton = binding.exportButton

        importButton.setOnClickListener {
            openDirectoryForImport()
        }

        exportButton.setOnClickListener {
            openDirectoryForExport()
        }

        return binding.root
    }

    private fun openDirectoryForImport() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, IMPORT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val treeUri = data?.data
            if (treeUri != null) {
                importDataFromUri(treeUri)
            }
        }

        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val treeUri = data?.data
            if (treeUri != null) {
                exportToSelectedFolder(treeUri)
            }
        }
    }

    private fun importDataFromUri(treeUri: Uri) {
        val treeDocumentFile = DocumentFile.fromTreeUri(requireContext(), treeUri)
        if (treeDocumentFile != null && treeDocumentFile.exists() && treeDocumentFile.isDirectory) {
            val destinationDir = File(
                requireContext().applicationInfo.dataDir,
                "databases"
            )

            clearDirectory(destinationDir)

            val filesInDirectory = treeDocumentFile.listFiles()
            if (filesInDirectory != null) {
                for (file in filesInDirectory) {
                    val sourceUri = file.uri
                    val sourceStream = requireContext().contentResolver.openInputStream(sourceUri)

                    if (sourceStream != null) {
                        val destinationFile = File(destinationDir, file.name)
                        val destinationStream = FileOutputStream(destinationFile)

                        try {
                            val buffer = ByteArray(1024)
                            var length: Int

                            while (sourceStream.read(buffer).also { length = it } > 0) {
                                destinationStream.write(buffer, 0, length)
                            }

                            Toast.makeText(
                                requireContext(),
                                "File ${file.name} imported successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(
                                requireContext(),
                                "Error importing file ${file.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } finally {
                            sourceStream.close()
                            destinationStream.close()
                        }
                    }
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun exportToSelectedFolder(treeUri: Uri) {
        val folder = DocumentFile.fromTreeUri(requireContext(), treeUri)
        if (folder != null && folder.exists() && folder.isDirectory) {
            val sourceDir = File(
                requireContext().applicationInfo.dataDir,
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
                                requireContext().contentResolver.openOutputStream(targetUri)
                            if (outputStream != null) {
                                try {
                                    val buffer = ByteArray(1024)
                                    var length: Int
                                    val sourceStream = FileInputStream(file)
                                    while (sourceStream.read(buffer).also { length = it } > 0) {
                                        outputStream.write(buffer, 0, length)
                                    }
                                    Toast.makeText(
                                        requireContext(),
                                        "${file.name} exported successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    Toast.makeText(
                                        requireContext(),
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

    private fun openDirectoryForExport() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

}
