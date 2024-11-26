package com.example.projectnailsschedule.presentation.settings

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.settings.faq.FaqDialogFragment
import com.example.projectnailsschedule.presentation.settings.themes.SelectThemeDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragmentCompat : PreferenceFragmentCompat() {
    private val log = this::class.simpleName
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val selectThemeFragmentTag = "SelectThemeFragment"
    private val faqFragmentTag = "FaqFragment"

    private lateinit var synchronization: SwitchPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        settingsViewModel.updateUserData("$log ${object {}.javaClass.enclosingMethod?.name}")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val changeTheme = findPreference<Preference>("theme")
        val sendFeedback = findPreference<Preference>("feedback")
        val faq = findPreference<Preference>("faq")
        synchronization = findPreference("synchronization")!!

        changeTheme?.setOnPreferenceClickListener {
            val dialogFragment = SelectThemeDialogFragment()
            dialogFragment.show(parentFragmentManager, selectThemeFragmentTag)
            true
        }

        sendFeedback?.setOnPreferenceClickListener {
            sendEmail()
            true
        }

        faq?.setOnPreferenceClickListener {
            val dialogFragment = FaqDialogFragment()
            dialogFragment.show(parentFragmentManager, faqFragmentTag)
            true
        }

        synchronization.setOnPreferenceClickListener {
            if (synchronization.isChecked) {
                showDialogSync()
            }
            true
        }
    }

    private fun showDialogSync() {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_warning_sync, null)
        dialog.setContentView(view)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val okButton = view.findViewById<Button>(R.id.ok_button)
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)

        okButton.setOnClickListener {
            synchronization.isChecked = true
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            synchronization.isChecked = false
            dialog.dismiss()
        }

        dialog.setOnCancelListener {
            synchronization.isChecked = false
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun sendEmail() {
        val recipientEmail = getString(R.string.support_email)

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$recipientEmail")

        startActivity(emailIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingsViewModel.updateUserData("$log ${object {}.javaClass.enclosingMethod?.name}")
    }
}