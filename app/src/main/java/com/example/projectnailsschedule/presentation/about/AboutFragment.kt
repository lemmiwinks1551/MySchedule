package com.example.projectnailsschedule.presentation.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAboutBinding
import com.example.projectnailsschedule.util.Util

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private var versionTextView: TextView? = null
    private var aboutViewModel: AboutViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        aboutViewModel =
            ViewModelProvider(this)[AboutViewModel::class.java]

        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        initWidgets()

        setVersionTextView()

        /** for test only !!!
         * add fake appointments */
        // Util().addTestData(requireContext())

        return binding.root
    }

    private fun initWidgets() {
        versionTextView = binding.versionTextView
    }

    private fun setVersionTextView() {
        val versionLabel = getString(R.string.app_version)
        val appVersion = "$versionLabel ${BuildConfig.VERSION_NAME}"
        versionTextView?.text = appVersion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}