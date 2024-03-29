package com.example.projectnailsschedule.presentation.clients.editClient

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ClientEditFragment : Fragment() {
    private val clientsViewModel: ClientsViewModel by activityViewModels()

    private var _binding: FragmentClientEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var name: EditText
    private lateinit var phone: EditText
    private lateinit var vk: EditText
    private lateinit var telegram: EditText
    private lateinit var instagram: EditText
    private lateinit var whatsapp: EditText
    private lateinit var notes: EditText

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
        name = binding.clientNameEt
        phone = binding.clientPhoneTv
        vk = binding.clientVkTv
        telegram = binding.clientTgTv
        instagram = binding.clientInstagramTv
        whatsapp = binding.clientWhatsappTv
        notes = binding.clientNoteTv
    }

    private fun inflateViews() {
        val selectedClient = clientsViewModel.selectedClient
        if (selectedClient != null) {
            with(selectedClient) {
                this@ClientEditFragment.name.setText(name)
                this@ClientEditFragment.phone.setText(phone)
                this@ClientEditFragment.vk.setText(vk)
                this@ClientEditFragment.telegram.setText(telegram)
                this@ClientEditFragment.instagram.setText(instagram)
                this@ClientEditFragment.whatsapp.setText(whatsapp)
                this@ClientEditFragment.notes.setText(notes)
            }
        }

        vk.setOnFocusChangeListener { _, _ ->
            if (vk.text.toString().contains("https://vk.com/")) {
                val shortUrl = Util().extractVkUsername(vk.text.toString())
                vk.setText(shortUrl)
            }
        }

        instagram.setOnFocusChangeListener { _, _ ->
            if (instagram.text.toString().contains("https://www.instagram.com/")) {
                val shortUrl = Util().extractInstagramUsername(instagram.text.toString())
                instagram.setText(shortUrl)
            }
        }
    }

    private fun setClickListeners() {
        saveToolbarButton.setOnMenuItemClickListener {
            val clientModelDb = ClientModelDb(
                _id = clientsViewModel.selectedClient?._id,
                name = name.text.toString(),
                phone = phone.text.toString(),
                vk = vk.text.toString(),
                telegram = telegram.text.toString(),
                instagram = instagram.text.toString(),
                whatsapp = whatsapp.text.toString(),
                notes = notes.text.toString()
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

        phone.setOnClickListener {
            //clientsViewModel.startPhone(phone.text.toString())
            showOptionsDialog(phone)
        }

        vk.setOnClickListener {
            showOptionsDialog(vk)
        }

        instagram.setOnClickListener {
            // clientsViewModel.startInstagram("https://${instagram.text}")
            showOptionsDialog(instagram)
        }

        telegram.setOnClickListener {
            /*            if (telegram.text.toString().contains("https://t.me/")) {
                clientsViewModel.startTelegram("https://t.me/" + telegram.text.toString())
            } else {
                clientsViewModel.startTelegram(telegram.text.toString())
            }*/
            showOptionsDialog(telegram)
        }

        whatsapp.setOnClickListener {
            //clientsViewModel.startWhatsApp(whatsapp.text.toString())
            showOptionsDialog(whatsapp)
        }

        phone.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                phone.clearFocus()
                phone.isFocusableInTouchMode = false

                phone.setOnClickListener {
                    showOptionsDialog(phone)
                }
            }
            false
        }

        phone.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                phone.clearFocus()
                phone.isFocusableInTouchMode = false

                phone.setOnClickListener {
                    showOptionsDialog(phone)
                }
            }
        }

        vk.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NONE) {
                vk.clearFocus()
                vk.isFocusableInTouchMode = false

                vk.setOnClickListener {
                    showOptionsDialog(vk)
                }
            }
            false
        }

        vk.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                vk.clearFocus()
                vk.isFocusableInTouchMode = false

                vk.setOnClickListener {
                    showOptionsDialog(vk)
                }
            }
        }

        instagram.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NONE) {
                instagram.clearFocus()
                instagram.isFocusableInTouchMode = false

                instagram.setOnClickListener {
                    showOptionsDialog(instagram)
                }
            }
            false
        }

        instagram.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                instagram.clearFocus()
                instagram.isFocusableInTouchMode = false

                instagram.setOnClickListener {
                    showOptionsDialog(instagram)
                }
            }
        }

        telegram.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NONE) {
                telegram.clearFocus()
                telegram.isFocusableInTouchMode = false

                telegram.setOnClickListener {
                    showOptionsDialog(telegram)
                }
            }
            false
        }

        telegram.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                telegram.clearFocus()
                telegram.isFocusableInTouchMode = false

                telegram.setOnClickListener {
                    showOptionsDialog(telegram)
                }
            }
        }

        whatsapp.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NONE) {
                whatsapp.clearFocus()
                whatsapp.isFocusableInTouchMode = false

                whatsapp.setOnClickListener {
                    showOptionsDialog(whatsapp)
                }
            }
            false
        }

        whatsapp.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                whatsapp.clearFocus()
                whatsapp.isFocusableInTouchMode = false

                whatsapp.setOnClickListener {
                    showOptionsDialog(whatsapp)
                }
            }
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
        if (vk.text.toString().contains("https://vk.com/")) {
            val shortUrl = Util().extractVkUsername(vk.text.toString())
            vk.setText(shortUrl)
        }

        if (instagram.text.toString().contains("https://www.instagram.com/")) {
            val shortUrl = Util().extractInstagramUsername(instagram.text.toString())
            instagram.setText(shortUrl)
        }

        if (telegram.text.toString().contains("https://t.me/")) {
            val shortUrl = Util().extractTelegramUsername(telegram.text.toString())
            telegram.setText(shortUrl)
        }
    }

    private fun showOptionsDialog(clickedView: EditText) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(view)

        val execute = view.findViewById<TextView>(R.id.execute)
        val edit = view.findViewById<TextView>(R.id.edit)

        execute.setOnClickListener {
            // Execute
        }

        when (clickedView.id) {
            phone.id -> {
                Log.i("EditViewClicked", "${phone.id} - phone")
            }
            vk.id -> {
                Log.i("EditViewClicked", "${vk.id} - vk")
            }
            telegram.id -> {
                Log.i("EditViewClicked", "${telegram.id} - tg")
            }
            instagram.id -> {
                Log.i("EditViewClicked", "${instagram.id} - inst")
            }
            whatsapp.id -> {
                Log.i("EditViewClicked", "${whatsapp.id} - whtsa")
            }
        }

        edit.setOnClickListener {
            // Edit
            clickedView.isFocusableInTouchMode = true
            clickedView.requestFocus()

            showKeyboard()

            clickedView.setOnClickListener(null)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun closeKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}

