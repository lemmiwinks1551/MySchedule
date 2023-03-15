package com.example.projectnailsschedule.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchAdapter
import java.util.*


class SearchFragment : Fragment(), SearchAdapter.OnItemListener {

    val log = this::class.simpleName

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var stringSearchTextView: SearchView? = null
    private var searchRecyclerView: RecyclerView? = null
    private var searchViewModel: SearchViewModel? = null

    private var appointmentArray: MutableList<AppointmentParams> = mutableListOf()
    private var noData = "Данные не найдены"

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
        searchViewModel?.getAllAppointments()

        return binding.root
    }

    private fun initWidgets() {
        // init widgets
        stringSearchTextView = binding.searchView
        searchRecyclerView = binding.searchRecyclerView
    }

    private fun initObservers() {
        searchViewModel?.appointmentArray?.observe(viewLifecycleOwner) {
            appointmentArray = it
            inflateSearchRecyclerVIew(appointmentArray)
        }
    }

    private fun initClickListeners() {
        stringSearchTextView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                searchViewModel?.getAllAppointments()
                filter(msg)
                return false
            }
        })
    }

    private fun inflateSearchRecyclerVIew(appointmentsList: MutableList<AppointmentParams>) {
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

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val appointmentsListFiltered: ArrayList<AppointmentParams> = ArrayList()
        val locale = Locale("ru")

        // running a for loop to compare elements.
        for (appointmentParams in appointmentArray) {
            // checking if the entered string matched with any appointmentParams of our recycler view.
            with(appointmentParams) {
                if (appointmentDate.toString().lowercase(locale).contains(text.lowercase(locale)) ||
                    clientName.toString().lowercase(locale).contains(text.lowercase(locale)) ||
                    phoneNum.toString().lowercase(locale).contains(text.lowercase(locale)) ||
                    startTime.toString().lowercase(locale).contains(text.lowercase(locale)) ||
                    misc.toString().lowercase(locale).contains(text.lowercase(locale))
                ) {
                    // if the appointmentParams is matched we are
                    // adding it to our filtered list.
                    appointmentsListFiltered.add(appointmentParams)
                }
            }
        }
        if (appointmentsListFiltered.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(context, noData, Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to recycler view
            inflateSearchRecyclerVIew(appointmentsListFiltered)
        }
    }

    override fun onItemClick(position: Int, dayText: String?) {
        // go into date

    }
}