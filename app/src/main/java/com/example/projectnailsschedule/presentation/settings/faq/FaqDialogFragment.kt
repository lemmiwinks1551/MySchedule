package com.example.projectnailsschedule.presentation.settings.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FaqLayoutBinding
import com.example.projectnailsschedule.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FaqDialogFragment : DialogFragment() {
    private val log = this::class.simpleName
    private val settingsViewModel: SettingsViewModel by viewModels()

    private var _binding: FaqLayoutBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: FaqRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FaqLayoutBinding.inflate(inflater, container, false)

        initViews()

        inflateRecyclerView()

        settingsViewModel.updateUserData("$log ${object{}.javaClass.enclosingMethod?.name}")
        return binding.root
    }

    private fun initViews() {
        recyclerView = binding.recyclerView
    }

    private fun inflateRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            recyclerViewAdapter = FaqRvAdapter(
                faq = settingsViewModel.getFaq()
            )

            withContext(Dispatchers.Main) {
                val layoutManager: RecyclerView.LayoutManager =
                    GridLayoutManager(activity, 1)

                recyclerView?.layoutManager = layoutManager
                recyclerView?.adapter = recyclerViewAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingsViewModel.updateUserData("$log ${object{}.javaClass.enclosingMethod?.name}")
        _binding = null
    }
}