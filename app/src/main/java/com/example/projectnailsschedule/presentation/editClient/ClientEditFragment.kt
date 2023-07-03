package com.example.projectnailsschedule.presentation.editClient

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
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

    private var clientToEdit: ClientModelDb? = null
    private var bindingKeyClientEdit = "editClient"

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
        setClickListeners()

        // get bindingKeyClientEdit from arguments
        if (arguments != null) {
            clientToEdit = arguments?.getParcelable(bindingKeyClientEdit)
            setFields()
        }

        return binding.root
    }

    private fun initWidgets() {
        nameEditText = binding.clientNameEditText
        phoneEditText = binding.clientPhoneEditText
        notesTEditText = binding.clientNotesEditText
        saveButton = binding.saveButton
        cancelButton = binding.cancelButton
    }

    private fun setFields() {
        // set fields into EditViews
        val editableName: Editable = nameEditText!!.editableText
        editableName.insert(nameEditText!!.selectionStart, clientToEdit!!.name)

        val editablePhone: Editable = phoneEditText!!.editableText
        editablePhone.insert(phoneEditText!!.selectionStart, clientToEdit!!.phone)

        val editableNotes: Editable = notesTEditText!!.editableText
        editableNotes.insert(notesTEditText!!.selectionStart, clientToEdit!!.notes)
    }

    private fun setClickListeners() {
        saveButton!!.setOnClickListener {
            val clientModelDb = ClientModelDb(
                _id = clientToEdit?._id,
                name = nameEditText?.text.toString(),
                phone = phoneEditText?.text.toString(),
                notes = notesTEditText?.text.toString()
            )

            if (clientToEdit != null) {
                clientEditViewModel?.updateClient(clientModelDb)
            } else {
                clientEditViewModel?.saveClient(clientModelDb)
            }

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

        // set phone input format on phone_edit_text
        binding.clientPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.callClientButton.setOnClickListener {
            val phone = binding.clientPhoneEditText.text.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}