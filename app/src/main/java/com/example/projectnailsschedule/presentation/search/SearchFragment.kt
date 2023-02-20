package com.example.projectnailsschedule.presentation.search

import android.database.SQLException
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.data.storage.ScheduleDbHelper
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchAdapter


class SearchFragment : Fragment(), SearchAdapter.OnItemListener {

    val log = this::class.simpleName

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var stringSearch: EditText? = null
    private var searchRecycler: RecyclerView? = null

    private var searchViewModel: SearchViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(context)
        )[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init observers
        initObservers()

        // init click listeners
        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        // init widgets
        stringSearch = binding.searchEditText
        searchRecycler = binding.searchRecyclerView
    }

    private fun initObservers() {

    }

    private fun initClickListeners() {

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, dayText: String?) {
        // клик с переходом в день

    }
}