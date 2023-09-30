package com.example.projectnailsschedule.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private var settingsViewModel: SettingsViewModel? = null

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

        // init click listeners
        initClickListeners()

        return binding.root
    }

    private fun initAllWidgets() {
    }


    private fun initClickListeners() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}