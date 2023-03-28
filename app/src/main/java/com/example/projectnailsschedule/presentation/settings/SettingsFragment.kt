package com.example.projectnailsschedule.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private var settingsViewModel: SettingsViewModel? = null

    private var themeSwitcher: SwitchCompat? = null
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

        // init all widgets
        initAllWidgets()

        setThemeSwitcher()

        // init click listeners
        initClickListeners()

        return binding.root
    }

    private fun initAllWidgets() {
        themeSwitcher = binding.themeSwitcher
    }

    private fun initClickListeners() {
        // set theme switcher click listener
        themeSwitcher?.setOnClickListener {
            if (themeSwitcher!!.isChecked) {
                settingsViewModel!!.setDarkTheme()
                themeSwitcher?.isChecked = true
            } else {
                settingsViewModel!!.setLightTheme()
                themeSwitcher?.isChecked = false
            }
            activity?.recreate()
        }
    }

    private fun setThemeSwitcher() {
        themeSwitcher?.isChecked = settingsViewModel?.darkThemeOn == true
    }
}
