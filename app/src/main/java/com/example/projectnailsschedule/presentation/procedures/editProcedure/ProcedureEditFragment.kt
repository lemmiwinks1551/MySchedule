package com.example.projectnailsschedule.presentation.procedures.editProcedure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentProcedureEditBinding
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.presentation.procedures.ProceduresViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProcedureEditFragment : Fragment() {
    private val log = this::class.simpleName
    private val proceduresViewModel: ProceduresViewModel by activityViewModels()

    private var _binding: FragmentProcedureEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var procedureNameEt: EditText
    private lateinit var procedurePriceEt: EditText
    private lateinit var procedureNotesEt: EditText

    private lateinit var saveToolbarButton: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcedureEditBinding.inflate(inflater, container, false)

        initViews()

        inflateViews()

        setHasOptionsMenu(true)

        proceduresViewModel.updateUserData("$log ${object{}.javaClass.enclosingMethod?.name}")

        return binding.root
    }

    private fun initViews() {
        procedureNameEt = binding.procedureNameEditText
        procedurePriceEt = binding.procedurePriceEditText
        procedureNotesEt = binding.procedureNotesEditText
    }

    private fun inflateViews() {
        val selectedProcedure = proceduresViewModel.selectedProcedure
        if (selectedProcedure != null) {
            with(selectedProcedure) {
                procedureNameEt.setText(procedureName)
                procedurePriceEt.setText(procedurePrice)
                procedureNotesEt.setText(procedureNotes)
            }
        }
    }

    private fun setClickListeners() {
        saveToolbarButton.setOnMenuItemClickListener {
            val procedureModelDb = ProcedureModelDb(
                _id = proceduresViewModel.selectedProcedure?._id,
                procedureName = procedureNameEt.text.toString(),
                procedurePrice = procedurePriceEt.text.toString(),
                procedureNotes = procedureNotesEt.text.toString()
            )

            lifecycleScope.launch {
                if (proceduresViewModel.selectedProcedure?._id != null) {
                    proceduresViewModel.updateProcedure(procedureModelDb)
                } else {
                    proceduresViewModel.insertProcedure(procedureModelDb)
                }
            }

            val toast: Toast = Toast.makeText(
                context,
                getString(R.string.procedure_created, procedureModelDb.procedureName),
                Toast.LENGTH_LONG
            )
            toast.show()

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        proceduresViewModel.selectedProcedure = null
        proceduresViewModel.updateUserData("$log ${object{}.javaClass.enclosingMethod?.name}")
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        saveToolbarButton = menu.findItem(R.id.save_toolbar_button)
        saveToolbarButton.isVisible = true
        setClickListeners()
        super.onPrepareOptionsMenu(menu)
    }
}