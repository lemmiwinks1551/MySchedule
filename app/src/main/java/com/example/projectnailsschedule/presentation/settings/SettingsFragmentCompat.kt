package com.example.projectnailsschedule.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragmentCompat : PreferenceFragmentCompat() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val changeTheme = findPreference<Preference>("theme")
        val sendFeedback = findPreference<Preference>("feedback")

        changeTheme?.setOnPreferenceClickListener {
            val themeName = "MyNewTheme"
            val themeName1 = "Theme.Main"
            settingsViewModel.setUserTheme(themeName1)
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
}