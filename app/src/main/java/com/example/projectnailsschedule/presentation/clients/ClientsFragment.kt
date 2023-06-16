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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentClientsBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.appointment.AppointmentViewModel
import com.example.projectnailsschedule.presentation.appointment.AppointmentViewModelFactory
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private var clientsViewModel: ClientsViewModel? = null

    private var clientsList: List<ClientModelDb>? = null
    private var clientsAdapter: ClientsAdapter? = null

    private var clientsSearchView: SearchView? = null
    private var searchClientsRecyclerView: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var clientsCountTextView :TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientsViewModel = ViewModelProvider(
            this,
            ClientsViewModelFactory(context)
        )[ClientsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentClientsBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init observers
        initObservers()

        // swipe to delete
        swipeToDelete()

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
                clientsViewModel!!.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
                    clientsList = list
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
        clientsAdapter = ClientsAdapter(
            clientsCount = clientsList.size,
            clientsFragment = this,
            clientsList = clientsList
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchClientsRecyclerView?.layoutManager = layoutManager
        searchClientsRecyclerView?.adapter = clientsAdapter
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deleteClientModelDb: ClientModelDb = clientsList!![viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition

            }

        })
    }

    override fun onResume() {
        clientsSearchView?.setQuery("", true) // clear search bar
        super.onResume()
    }

    override fun onPause() {
        clientsSearchView?.setQuery("", true) // clear search bar
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}