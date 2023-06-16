package com.example.projectnailsschedule.presentation.editClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.databinding.FragmentClientEditBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb

class ClientEditFragment : Fragment() {

    private var _binding: FragmentClientEditBinding? = null
    private val binding get() = _binding!!

    private var clientEditViewModel: ClientEditViewModel? = null

    private var nameEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var notesTEditText: EditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        clientEditViewModel = ViewModelProvider(
            this,
            ClientsEditViewModelFactory(context)
        )[ClientEditViewModel::class.java]

        _binding = FragmentClientEditBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init clickListeners
        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        nameEditText = binding.clientNameEditText
        phoneEditText = binding.clientPhoneEditText
        notesTEditText = binding.clientNotesEditText
        saveButton = binding.saveButton
        cancelButton = binding.cancelButton
    }

    private fun initClickListeners() {
        saveButton!!.setOnClickListener {
            val clientModelDb = ClientModelDb(
                name = nameEditText?.text.toString(),
                phone = phoneEditText?.text.toString(),
                notes = notesTEditText?.text.toString()
            )
            clientEditViewModel?.saveClient(clientModelDb)

            val toast: Toast = Toast.makeText(
                context,
                "Клиент ${clientModelDb.name}\n${"создан."}",
                Toast.LENGTH_LONG
            )
            toast.show()

            // Return to previous screen
            findNavController().popBackStack()
        }

        cancelButton?.setOnClickListener {
            // Return to previous screen
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}