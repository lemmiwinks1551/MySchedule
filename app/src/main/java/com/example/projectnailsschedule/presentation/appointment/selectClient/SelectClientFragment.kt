package com.example.projectnailsschedule.presentation.appointment.selectClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSelectClientBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.appointment.selectClient.selectClientRV.SelectClientRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectClientFragment : DialogFragment() {
    val log = this::class.simpleName

    private val selectClientViewModel: SelectClientViewModel by viewModels()

    private var _binding: FragmentSelectClientBinding? = null
    private var clientSearchRV: RecyclerView? = null
    private var clientsSearchView: SearchView? = null

    private val binding get() = _binding!!

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
                lifecycleScope.launch {
                    inflateSearchRecyclerVIew(selectClientViewModel.searchClients(searchQuery))
                }
                return false
            }
        })
    }

    private fun inflateSearchRecyclerVIew(clientsList: List<ClientModelDb>) {
        // create adapter
        val selectClientRVAdapter = SelectClientRVAdapter(
            clientsCount = clientsList.size,
            clientsList = clientsList,
            selectClientViewModel = selectClientViewModel
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        clientSearchRV?.layoutManager = layoutManager
        clientSearchRV?.adapter = selectClientRVAdapter

        // set clickListener on clientsRV
        selectClientRVAdapter.setOnItemClickListener(object :
            SelectClientRVAdapter.OnItemClickListener {
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

    override fun onResume() {
        clientsSearchView?.setQuery(null, true) // clear search bar
        super.onResume()
    }

    override fun onPause() {
        clientsSearchView?.setQuery(null, true) // clear search bar
        super.onPause()
    }
}