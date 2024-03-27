package com.example.projectnailsschedule.presentation.clients.editClient

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
import com.example.projectnailsschedule.databinding.FragmentClientEditBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel
import com.example.projectnailsschedule.util.Util
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

        replaceUrls()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initViews() {
        nameEt = binding.clientNameEt
        phoneEt = binding.clientPhoneEt
        vkEditText = binding.clientVkEt
        telegramEt = binding.clientTgEt
        instagramEt = binding.clientInstagramEt
        whatsappEt = binding.clientWhatsappEt
        notesEt = binding.clientNoteEt
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

        vkEditText.setOnFocusChangeListener { v, hasFocus ->
            if (vkEditText.text.toString().contains("https://vk.com/")) {
                val shortUrl = Util().extractVkUsername(vkEditText.text.toString())
                vkEditText.setText(shortUrl)
            }

            if (instagramEt.text.toString().contains("https://www.instagram.com/")) {
                val shortUrl = Util().extractInstagramUsername(instagramEt.text.toString())
                instagramEt.setText(shortUrl)
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

        phoneEt.setOnClickListener {
            clientsViewModel.startPhone(phoneEt.text.toString())
        }

        vkEditText.setOnClickListener {
            clientsViewModel.startVk("https://${vkEditText.text}")
        }

        instagramEt.setOnClickListener {
            clientsViewModel.startInstagram("https://${instagramEt.text}")
        }

        telegramEt.setOnClickListener {
            if (telegramEt.text.toString().contains("https://t.me/")) {
                clientsViewModel.startTelegram("https://t.me/" + telegramEt.text.toString())
            } else {
                clientsViewModel.startTelegram(telegramEt.text.toString())
            }
        }

        whatsappEt.setOnClickListener {
            clientsViewModel.startWhatsApp(whatsappEt.text.toString())
        }
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

    private fun replaceUrls() {
        if (vkEditText.text.toString().contains("https://vk.com/")) {
            val shortUrl = Util().extractVkUsername(vkEditText.text.toString())
            vkEditText.setText(shortUrl)
        }

        if (instagramEt.text.toString().contains("https://www.instagram.com/")) {
            val shortUrl = Util().extractInstagramUsername(instagramEt.text.toString())
            instagramEt.setText(shortUrl)
        }

        if (telegramEt.text.toString().contains("https://t.me/")) {
            val shortUrl = Util().extractTelegramUsername(telegramEt.text.toString())
            telegramEt.setText(shortUrl)
        }
    }
}