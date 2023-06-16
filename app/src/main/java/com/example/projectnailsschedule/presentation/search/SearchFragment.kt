package com.example.projectnailsschedule.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchAdapter
import com.example.projectnailsschedule.util.Util

class SearchFragment : Fragment(), SearchAdapter.OnItemListener {

    private val log = this::class.simpleName
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchTextView: SearchView? = null
    private var searchRecyclerView: RecyclerView? = null
    private var searchViewModel: SearchViewModel? = null
    private var appointmentCount: TextView? = null

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
        searchTextView = binding.searchView
        searchRecyclerView = binding.searchRecyclerView
        appointmentCount = binding.appointmentsCountTextView
    }

    private fun initObservers() {
        searchViewModel!!.appointmentCount.observe(viewLifecycleOwner) {
            appointmentCount?.text = getString(R.string.appointments_count, it)
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
                searchViewModel!!.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
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
            searchViewModel = searchViewModel!!
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchRecyclerView?.layoutManager = layoutManager
        searchRecyclerView?.adapter = searchAdapter
    }

    override fun onItemClick(position: Int, dayText: String?) {
        val date = DateParams(
            _id = null,
            date = Util().convertStrToLocalDate(dayText!!),
            appointmentCount = null
        )

        val bundle = Bundle()
        bundle.putParcelable("dateParams", date)
        requireActivity().findNavController(R.id.appointments_count_text_view).navigate(
            R.id.action_nav_search_to_nav_date,
            bundle
        )
    }

    override fun onResume() {
        searchTextView?.setQuery("", true) // clear search bar
        super.onResume()
    }

    override fun onPause() {
        searchTextView?.setQuery("", true) // clear search bar
        super.onPause()
    }
}