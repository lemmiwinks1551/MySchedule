package com.example.projectnailsschedule.presentation.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAboutBinding
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutFragment : Fragment() {
    private val log = this::class.simpleName
    private val aboutViewModel: AboutViewModel by viewModels()

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private var versionTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        initWidgets()

        setVersionTextView()

/*        if (BuildConfig.DEBUG) {
            CoroutineScope(Dispatchers.IO).launch {
                addTestData()
            }
        }*/

        aboutViewModel.updateUserData("$log ${object {}.javaClass.enclosingMethod?.name}")

        return binding.root
    }

    private fun initWidgets() {
        versionTextView = binding.versionTextView

        binding.privacyPolicy.setOnClickListener {
            val url = "https://myschedule.myddns.me/privacy-policy"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        }
        binding.disclaimer.setOnClickListener {
            val url = "https://myschedule.myddns.me/disclaimer"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        }
    }

    private fun setVersionTextView() {
        val versionLabel = getString(R.string.app_version)
        val appVersion = "$versionLabel ${BuildConfig.VERSION_NAME}"
        versionTextView?.text = appVersion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        aboutViewModel.updateUserData("$log ${object {}.javaClass.enclosingMethod?.name}")
        _binding = null
    }

    private suspend fun addTestData() {
        // Запускаем загрузку данных
        if (BuildConfig.DEBUG) {
            Log.d("Test", "Debug")
            /** for test only !!!
             * add fake appointments */
            Util().addTestData(requireContext())
            Util().createTestClients(requireContext())
            Util().createTestProcedures(requireContext())
        } else {
            Log.d("Test", "Prod")
        }
    }
}