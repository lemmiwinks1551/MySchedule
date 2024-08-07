package com.example.projectnailsschedule.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.settings.themesRV.SelectThemeDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragmentCompat : PreferenceFragmentCompat() {
    private val log = this::class.simpleName
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val selectThemeFragmentTag = "SelectThemeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        settingsViewModel.updateUserData("$log ${object{}.javaClass.enclosingMethod?.name}")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val changeTheme = findPreference<Preference>("theme")
        val sendFeedback = findPreference<Preference>("feedback")

        changeTheme?.setOnPreferenceClickListener {
            val dialogFragment = SelectThemeDialogFragment()
            dialogFragment.show(parentFragmentManager, selectThemeFragmentTag)
            true
        }

        sendFeedback?.setOnPreferenceClickListener {
            sendEmail()
            true
        }
    }

    private fun sendEmail() {
        val recipientEmail = getString(R.string.support_email)

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$recipientEmail")

        startActivity(emailIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingsViewModel.updateUserData("$log ${object{}.javaClass.enclosingMethod?.name}")
    }
}