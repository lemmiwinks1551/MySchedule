package com.example.projectnailsschedule.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchAdapter
import com.example.projectnailsschedule.util.Util
import java.time.format.DateTimeFormatter
import java.util.*

class SearchFragment : Fragment(), SearchAdapter.OnItemListener {

    val log = this::class.simpleName

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var stringSearchTextView: SearchView? = null
    private var searchRecyclerView: RecyclerView? = null
    private var searchViewModel: SearchViewModel? = null

    private var appointmentArray: MutableList<AppointmentModelDb> = mutableListOf()
    private var noDataToast = "Данные не найдены"
    private var prevText = ""

    private var scheduleDb: ScheduleDb? = null

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

        // load appointments list
        scheduleDb = ScheduleDb.getDb(requireContext())

        scheduleDb!!.getDao().selectAll().asLiveData().observe(viewLifecycleOwner) {
            inflateSearchRecyclerVIew(it)
        }
        return binding.root
    }

    private fun initWidgets() {
        stringSearchTextView = binding.searchView
        searchRecyclerView = binding.searchRecyclerView
    }

    private fun initObservers() {
    }

    private fun initClickListeners() {
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
}