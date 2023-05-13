package com.example.projectnailsschedule.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchAdapter

class SearchFragment : Fragment(), SearchAdapter.OnItemListener {

    val log = this::class.simpleName

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchTextView: SearchView? = null
    private var searchRecyclerView: RecyclerView? = null
    private var searchViewModel: SearchViewModel? = null

    private var appointmentArray: MutableList<AppointmentModelDb> = mutableListOf()
    private var noDataToast = "Данные не найдены"
    private var prevQuery = ""

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
        searchViewModel?.scheduleDb = ScheduleDb.getDb(requireContext())

        // init widgets
        initWidgets()

        // init observers
        initObservers()

        // init click listeners
        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        searchTextView = binding.searchView
        searchRecyclerView = binding.searchRecyclerView
    }

    private fun initObservers() {
        searchViewModel!!.getAllAppointments()?.observe(requireActivity()) { list ->
            inflateSearchRecyclerVIew(list)
        }
    }

    private fun initClickListeners() {
        searchTextView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                prevQuery = newText!!
                searchViewModel!!.searchDatabase(searchQuery)?.observe(requireActivity()) { list ->
                    inflateSearchRecyclerVIew(list)
                }
                return false
            }
        })
    }

    private fun inflateSearchRecyclerVIew(appointmentsList: List<AppointmentModelDb>) {
        // create adapter
        val searchAdapter = SearchAdapter(
            appointmentCount = appointmentsList.size,
            searchFragment = this,
            appointmentsList = appointmentsList,
            fragmentActivity = requireActivity(),
            searchViewModel = searchViewModel!!
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchRecyclerView?.layoutManager = layoutManager
        searchRecyclerView?.adapter = searchAdapter
    }

    override fun onItemClick(position: Int, dayText: String?) {
        //
    }

    override fun onResume() {
        searchTextView?.setQuery(null, true) // clear search bar
        super.onResume()
    }
}