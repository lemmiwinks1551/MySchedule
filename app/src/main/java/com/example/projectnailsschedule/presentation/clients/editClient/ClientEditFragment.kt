package com.example.projectnailsschedule.presentation.clients.editClient

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
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
import com.example.projectnailsschedule.databinding.FragmentClientEditBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientEditFragment : Fragment() {
    private val clientsViewModel: ClientsViewModel by activityViewModels()

    private var _binding: FragmentClientEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var nameEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var vkEditText: EditText
    private lateinit var telegramEt: EditText
    private lateinit var instagramEt: EditText
    private lateinit var whatsappEt: EditText
    private lateinit var notesEt: EditText

    private lateinit var saveToolbarButton: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientEditBinding.inflate(inflater, container, false)

        initViews()

        inflateViews()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initViews() {
        nameEt = binding.clientNameEditText
        phoneEt = binding.clientPhoneEditText
        vkEditText = binding.clientVkLinkEt
        telegramEt = binding.clientTelegramLinkEt
        instagramEt = binding.clientInstagramLinkEt
        whatsappEt = binding.clientWhatsappLinkEt
        notesEt = binding.clientNotesEditText
    }

    private fun inflateViews() {
        val selectedClient = clientsViewModel.selectedClient
        if (selectedClient != null) {
            with(selectedClient) {
                nameEt.setText(name)
                phoneEt.setText(phone)
                vkEditText.setText(vk)
                telegramEt.setText(telegram)
                instagramEt.setText(instagram)
                whatsappEt.setText(whatsapp)
                notesEt.setText(notes)
            }
        }
    }

    private fun setClickListeners() {
        saveToolbarButton.setOnMenuItemClickListener {
            val clientModelDb = ClientModelDb(
                _id = clientsViewModel.selectedClient?._id,
                name = nameEt.text.toString(),
                phone = phoneEt.text.toString(),
                vk = vkEditText.text.toString(),
                telegram = telegramEt.text.toString(),
                instagram = instagramEt.text.toString(),
                whatsapp = whatsappEt.text.toString(),
                notes = notesEt.text.toString()
            )

            lifecycleScope.launch {
                if (clientsViewModel.selectedClient?._id != null) {
                    clientsViewModel.updateClient(clientModelDb)
                } else {
                    clientsViewModel.insertClient(clientModelDb)
                }
            }

            val toast: Toast = Toast.makeText(
                context,
                getString(R.string.client_created, clientModelDb.name),
                Toast.LENGTH_LONG
            )
            toast.show()

            findNavController().popBackStack()
        }

        binding.clientPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clientsViewModel.selectedClient = null
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        saveToolbarButton = menu.findItem(R.id.save_toolbar_button)
        saveToolbarButton.isVisible = true
        setClickListeners()
        super.onPrepareOptionsMenu(menu)
    }
}