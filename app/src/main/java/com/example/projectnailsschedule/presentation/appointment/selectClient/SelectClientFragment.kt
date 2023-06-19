package com.example.projectnailsschedule.presentation.appointment.selectClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAppointmentBinding
import com.example.projectnailsschedule.databinding.FragmentSelectClientBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.appointment.selectClient.selectClientRV.SelectClientRVAdapter
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsAdapter

class SelectClientFragment : Fragment() {
    val log = this::class.simpleName

    private var selectClientViewModel: SelectClientViewModel? = null

    private var _binding: FragmentSelectClientBinding? = null
    private var clientSearchRV: RecyclerView? = null
    private var clientsSearchView: SearchView? = null

    private val binding get() = _binding!!

    private val bindingKeyClientEdit = "selectedClient"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        selectClientViewModel = ViewModelProvider(
            this,
            SelectClientViewModelFactory(context)
        )[SelectClientViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // set binding
        _binding = FragmentSelectClientBinding.inflate(inflater, container, false)

        initWidgets()

        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        clientSearchRV = binding.clientsRecyclerView
        clientsSearchView = binding.selectClientSearchView
    }

    private fun initClickListeners() {
        binding.selectClientSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                selectClientViewModel!!.searchClients(searchQuery)
                    .observe(viewLifecycleOwner) { list ->
                        inflateSearchRecyclerVIew(list)
                    }
                return false
            }
        })
    }

    private fun inflateSearchRecyclerVIew(clientsList: List<ClientModelDb>) {
        // create adapter
        val selectClientRVAdapter = SelectClientRVAdapter(
            clientsCount = clientsList.size,
            clientsList = clientsList
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        clientSearchRV?.layoutManager = layoutManager
        clientSearchRV?.adapter = selectClientRVAdapter

        // set clickListener on clientsRV
        selectClientRVAdapter.setOnItemClickListener(object :
            SelectClientRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected client
                val bundle = Bundle()
                val clientModelDb = ClientModelDb(
                    _id = clientsList[position]._id,
                    name = clientsList[position].name,
                    phone = clientsList[position].phone,
                    notes = clientsList[position].notes
                )
                val navController = findNavController()
                bundle.putParcelable(bindingKeyClientEdit, clientModelDb)
                navController.navigate(R.id.action_selectClient_to_nav_appointment, bundle)
            }
        })
    }

    override fun onResume() {
        clientsSearchView?.setQuery(null, true) // clear search bar
        super.onResume()
    }

    override fun onPause() {
        clientsSearchView?.setQuery(null, true) // clear search bar
        super.onPause()
    }
}