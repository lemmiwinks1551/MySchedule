package com.example.projectnailsschedule.presentation.about

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAboutBinding
import com.example.projectnailsschedule.domain.repository.isDayOffApi
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

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

        /*        CoroutineScope(Dispatchers.IO).launch {
                    addTestData()
                }*/

        // retrofit test
        val retrofit = Retrofit.Builder()
            .baseUrl("https://isdayoff.ru")
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        val isDayOffApi = retrofit.create(isDayOffApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            Log.i("Retrofit", "Start")
            val response = isDayOffApi.getDayStatus()
            val result = response.use { it.string() }
            withContext(Dispatchers.Main) {
                Log.i("Retrofit", result.toString())
            }
            Log.i("Retrofit", "End")
        }

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

    private suspend fun addTestData() {
        /** for test only !!!
         * add fake appointments */
        Util().addTestData(requireContext())
        Util().createTestClients(requireContext())
        Util().createTestProcedures(requireContext())
    }
}