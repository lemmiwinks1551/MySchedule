package com.example.projectnailsschedule.presentation.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentClientsBinding
import com.example.projectnailsschedule.databinding.SelectUnifBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.ClientsRvAdapter
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SelectClientFragment : DialogFragment() {
    private val clientsViewModel: ClientsViewModel by activityViewModels()

    private var _binding: SelectUnifBinding? = null
    private val binding get() = _binding!!

    private var clientsList: MutableList<ClientModelDb>? = null
    private var clientsRvAdapter: ClientsRvAdapter? = null

    private var searchView: SearchView? = null
    private var searchClientsRV: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectUnifBinding.inflate(inflater, container, false)

        initViews()

        initClickListeners()

        clearSearchView()

        return binding.root
    }

    private fun initViews() {
        searchView = binding.searchView
        searchClientsRV = binding.recyclerView
        binding.floatingActionButton.visibility = View.GONE
    }

    private fun initClickListeners() {
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                lifecycleScope.launch(Dispatchers.IO) {
                    clientsList = async { clientsViewModel.searchClient(searchQuery) }.await()
                    withContext(Dispatchers.Main) {
                        inflateRecyclerView(clientsList!!)
                    }
                }
                return false
            }
        })
    }

    private fun inflateRecyclerView(clientsList: List<ClientModelDb>) {
        // create adapter
        clientsRvAdapter = ClientsRvAdapter(
            clientsList = clientsList,
            clientsViewModel = clientsViewModel
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchClientsRV?.layoutManager = layoutManager
        searchClientsRV?.adapter = clientsRvAdapter

        // set clickListener on clientsRV
        clientsRvAdapter?.setOnItemClickListener(object :
            ClientsRvAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // client selected
                val clientModelDb = ClientModelDb(
                    _id = clientsList[position]._id,
                    name = clientsList[position].name,
                    phone = clientsList[position].phone,
                    vk = clientsList[position].vk,
                    telegram = clientsList[position].telegram,
                    instagram = clientsList[position].instagram,
                    whatsapp = clientsList[position].whatsapp,
                    notes = clientsList[position].notes
                )

                findNavController().currentBackStackEntry?.savedStateHandle?.set("client", clientModelDb)
                dismiss() // закрыть диалоговое окно после передачи данных
            }
        })
    }

    private fun clearSearchView() {
        searchView?.setQuery(null, true)
    }
}