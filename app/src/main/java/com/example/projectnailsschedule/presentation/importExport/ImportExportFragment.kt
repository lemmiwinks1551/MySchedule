package com.example.projectnailsschedule.presentation.importExport

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentImportExportBinding

class ImportExportFragment : Fragment() {

    private lateinit var importExportViewModel: ImportExportViewModel
    private val OPEN_DIRECTORY_REQUEST_CODE = 123
    private val IMPORT_REQUEST_CODE = 789

    private lateinit var exportButton: Button
    private lateinit var importButton: Button

    private var _binding: FragmentImportExportBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        importExportViewModel = ViewModelProvider(
            this,
            ImportExportViewModelFactory(context)
        )[ImportExportViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportExportBinding.inflate(inflater, container, false)

        initViews()

        initClickListeners()

        return binding.root
    }

    private fun initViews() {
        importButton = binding.importButton
        exportButton = binding.exportButton
    }

    private fun initClickListeners() {
        importButton.setOnClickListener {
            openDirectoryForImport()
        }

        exportButton.setOnClickListener {
            openDirectoryForExport()
        }
    }

    private fun openDirectoryForImport() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, IMPORT_REQUEST_CODE)
    }

    private fun openDirectoryForExport() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val treeUri = data?.data
            if (treeUri != null) {
                import(treeUri)
            }
        }

        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val treeUri = data?.data
            if (treeUri != null) {
                export(treeUri)
            }
        }
    }

    private fun import(treeUri: Uri) {
        importExportViewModel.import(treeUri)

        Toast.makeText(
            requireContext(),
            R.string.imported,
            Toast.LENGTH_SHORT
        ).show()

        importExportViewModel.restartApp()
    }

    private fun export(treeUri: Uri) {
        importExportViewModel.export(treeUri)

        Toast.makeText(
            requireContext(),
            R.string.exported,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}