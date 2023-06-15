package com.example.projectnailsschedule.presentation.clients

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
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.databinding.FragmentClientsBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsAdapter
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private var clientsViewModel: ClientsViewModel? = null

    private var clientsSearchView: SearchView? = null
    private var searchClientsRecyclerView: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var clientsCountTextView :TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        clientsViewModel = ViewModelProvider(
            this,
            ClientsViewModelFactory(context)
        )[ClientsViewModel::class.java]

        _binding = FragmentClientsBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init observers\
        initObservers()

        // initClickListeners
        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        clientsSearchView = binding.clientsSearchView
        searchClientsRecyclerView = binding.clientsRecyclerView
        addButton = binding.addButton
        clientsCountTextView = binding.clientsCountTextView
    }

    private fun initObservers() {
        clientsViewModel!!.clientsCount.observe(viewLifecycleOwner) {
            clientsCountTextView?.text = getString(R.string.appointments_count, it)
        }
    }

    private fun initClickListeners() {
        // search panel listener
        clientsSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                clientsViewModel!!.searchDatabase(searchQuery)?.observe(viewLifecycleOwner) { list ->
                    inflateClientsRecyclerView(list)
                }
                return false
            }
        })

        // add new client
        binding.addButton.setOnClickListener {
            binding.addButton.findNavController().navigate(
                R.id.action_nav_clients_to_nav_client_edit_fragment
            )
        }
    }

    private fun inflateClientsRecyclerView(clientsList: List<ClientModelDb>) {
        // create adapter
        val clientsAdapter = ClientsAdapter(
            clientsCount = clientsList.size,
            clientsFragment = this,
            clientsList = clientsList,
            clientsViewModel = clientsViewModel!!
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchClientsRecyclerView?.layoutManager = layoutManager
        searchClientsRecyclerView?.adapter = clientsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}