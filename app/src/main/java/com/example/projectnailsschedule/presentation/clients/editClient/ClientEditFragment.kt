package com.example.projectnailsschedule.presentation.clients.editClient

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

    lateinit var clientEditViewModel: ClientEditViewModel

    private var clientToEdit: ClientModelDb? = null
    private var bindingKeyClientEdit = "editClient"

    lateinit var nameEt: EditText
    lateinit var phoneEt: EditText
    private lateinit var vkEditText: EditText
    private lateinit var telegramEt: EditText
    private lateinit var instagramEt: EditText
    private lateinit var whatsappEt: EditText
    lateinit var notesEt: EditText

    lateinit var saveButton: Button
    lateinit var cancelButton: Button

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
        nameEt = binding.clientNameEditText
        phoneEt = binding.clientPhoneEditText
        vkEditText = binding.clientVkLinkEt
        telegramEt = binding.clientTelegramLinkEt
        instagramEt = binding.clientInstagramLinkEt
        whatsappEt = binding.clientWhatsappLinkEt
        notesEt = binding.clientNotesEditText

        saveButton = binding.saveButton
        cancelButton = binding.cancelButton
    }

    private fun setFields() {
        val fields = mapOf(
            nameEt to clientToEdit!!.name,
            phoneEt to clientToEdit!!.phone,
            vkEditText to clientToEdit!!.vk,
            telegramEt to clientToEdit!!.telegram,
            instagramEt to clientToEdit!!.instagram,
            whatsappEt to clientToEdit!!.whatsapp,
            notesEt to clientToEdit!!.notes
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
        saveButton.setOnClickListener {
            val clientModelDb = ClientModelDb(
                _id = clientToEdit?._id,
                name = nameEt.text.toString(),
                phone = phoneEt.text.toString(),
                vk = vkEditText.text.toString(),
                telegram = telegramEt.text.toString(),
                instagram = instagramEt.text.toString(),
                whatsapp = whatsappEt.text.toString(),
                notes = notesEt.text.toString()
            )

            if (clientToEdit != null) {
                clientEditViewModel.updateClient(clientModelDb)
            } else {
                clientEditViewModel.saveClient(clientModelDb)
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

        cancelButton.setOnClickListener {
            // Return to previous screen
            findNavController().popBackStack()
        }

        // set phone input format on phone_edit_text
        binding.clientPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}