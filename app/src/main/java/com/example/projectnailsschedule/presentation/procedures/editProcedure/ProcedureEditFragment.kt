package com.example.projectnailsschedule.presentation.procedures.editProcedure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentProcedureEditBinding
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProcedureEditFragment : Fragment() {
    private val procedureEditViewModel: ProcedureEditViewModel by viewModels()

    private var _binding: FragmentProcedureEditBinding? = null
    private val binding get() = _binding!!

    private var procedureToEdit: ProcedureModelDb? = null
    private var bindingKeyProcedureEdit = "editProcedure"

    lateinit var procedureNameTv: TextView
    lateinit var procedurePriceTv: TextView
    lateinit var procedureNotesTv: TextView

    lateinit var procedureNameEt: EditText
    lateinit var procedurePriceEt: EditText
    lateinit var procedureNotesEt: EditText

    private lateinit var saveToolbarButton: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcedureEditBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // get bindingKeyClientEdit from arguments
        if (arguments != null) {
            procedureToEdit = arguments?.getParcelable(bindingKeyProcedureEdit)
            setFields()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initWidgets() {
        procedureNameTv = binding.procedureNameTextView
        procedurePriceTv = binding.procedurePrice
        procedureNotesTv = binding.procedureNotesTextView

        procedureNameEt = binding.procedureNameEditText
        procedurePriceEt = binding.procedurePriceEditText
        procedureNotesEt = binding.procedureNotesEditText
    }

    private fun setFields() {
        val fields = mapOf(
            procedureNameEt to procedureToEdit!!.procedureName,
            procedurePriceEt to procedureToEdit!!.procedurePrice,
            procedureNotesEt to procedureToEdit!!.procedureNotes
        )

        for ((editText, value) in fields) {
            value?.let {
                val editable = editText.editableText
                val selectionStart = editText.selectionStart
                editable.insert(selectionStart, it)
            }
        }
    }

    private fun setClickListeners() {
        saveToolbarButton.setOnMenuItemClickListener {
            val procedureModelDb = ProcedureModelDb(
                _id = procedureToEdit?._id,
                procedureName = procedureNameEt.text.toString(),
                procedurePrice = procedurePriceEt.text.toString(),
                procedureNotes = procedureNotesEt.text.toString()
            )

            if (procedureToEdit != null) {
                lifecycleScope.launch {
                    procedureEditViewModel.updateProcedure(procedureModelDb)
                }
            } else {
                lifecycleScope.launch {
                    procedureEditViewModel.insertProcedure(procedureModelDb)
                }
            }

            val toast: Toast = Toast.makeText(
                context,
                getString(R.string.procedure_created, procedureModelDb.procedureName),
                Toast.LENGTH_LONG
            )
            toast.show()

            // Return to previous screen
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        saveToolbarButton = menu.findItem(R.id.save_toolbar_button)
        saveToolbarButton.isVisible = true
        setClickListeners()
        super.onPrepareOptionsMenu(menu)
    }
}