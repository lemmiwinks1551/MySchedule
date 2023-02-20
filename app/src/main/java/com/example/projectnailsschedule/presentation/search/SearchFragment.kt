package com.example.projectnailsschedule.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchAdapter


class SearchFragment : Fragment(), SearchAdapter.OnItemListener {

    val log = this::class.simpleName

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var stringSearchTextView: SearchView? = null
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
        stringSearchTextView = binding.searchView
        searchRecycler = binding.searchRecyclerView
    }

    private fun initObservers() {

    }

    private fun initClickListeners() {
        stringSearchTextView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                return false
            }
        })
    }

    override fun onItemClick(position: Int, dayText: String?) {
        // клик с переходом в день

    }
}