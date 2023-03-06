package com.example.projectnailsschedule.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private var settingsViewModel: SettingsViewModel? = null

    private var themeSwitcher: SwitchCompat? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // create ViewModel object with Factory
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(this.context)
        )[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        initAllWidgets()

        initClickListeners()

        return binding.root
    }

    private fun initAllWidgets() {
        themeSwitcher = binding.themeSwithcer
        // TODO: привести в порядок, сделать красиво, чтобы при включении приложения тема сразу менялась на актуальную, а не после захода во фрагмент 
        if (settingsViewModel!!.loadTheme()) {
            settingsViewModel!!.setDarkTheme()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            settingsViewModel!!.setLightTheme()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun initClickListeners() {
        themeSwitcher?.setOnClickListener {
            if (themeSwitcher!!.isChecked) {
                settingsViewModel!!.setDarkTheme()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                settingsViewModel!!.setLightTheme()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        }
    }
}
