package com.example.projectnailsschedule.presentation.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ClientsFragment : Fragment() {
    val log = this::class.simpleName

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private var clientsViewModel: ClientsViewModel? = null

    private var clientsList: List<ClientModelDb>? = null
    private var clientsRVAdapter: ClientsAdapter? = null

    private var clientsSearchView: SearchView? = null
    private var searchClientsRV: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var clientsCountTextView: TextView? = null

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
        searchClientsRV = binding.clientsRecyclerView
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
        clientsRVAdapter = ClientsAdapter(
            clientsCount = clientsList.size,
            clientsList = clientsList
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchClientsRV?.layoutManager = layoutManager
        searchClientsRV?.adapter = clientsRVAdapter

        // set clickListener on clientsRV
        clientsRVAdapter?.setOnItemClickListener(object : ClientsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    requireContext(),
                    clientsList?.get(position)?.name.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
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

                // delete client from Db
                clientsViewModel?.deleteClient(deleteClientModelDb)

                clientsRVAdapter?.notifyItemRemoved(position)

                // display Snackbar
                Snackbar.make(
                    searchClientsRV!!,
                    "Deleted " + deleteClientModelDb.name,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo"
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        clientsViewModel?.saveClient(deleteClientModelDb)

                        // below line is to notify item is
                        // added to our adapter class.

                        clientsRVAdapter?.notifyDataSetChanged()
                    }.show()
            }
        }).attachToRecyclerView(searchClientsRV)
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