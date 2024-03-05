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
import com.example.projectnailsschedule.databinding.FragmentProceduresBinding
import com.example.projectnailsschedule.databinding.SelectUnifBinding
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.presentation.procedures.ProceduresRvAdapter
import com.example.projectnailsschedule.presentation.procedures.ProceduresViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SelectProcedureFragment : DialogFragment() {
    private val proceduresViewModel: ProceduresViewModel by activityViewModels()

    private var _binding: SelectUnifBinding? = null
    private val binding get() = _binding!!

    private var proceduresList: MutableList<ProcedureModelDb>? = null
    private var proceduresRVAdapter: ProceduresRvAdapter? = null

    private var searchView: SearchView? = null
    private var searchProceduresRV: RecyclerView? = null

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
        searchProceduresRV = binding.recyclerView
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
                    proceduresList =
                        async { proceduresViewModel.searchProcedure(searchQuery) }.await()
                    withContext(Dispatchers.Main) {
                        inflateRecyclerView(proceduresList!!)
                    }
                }
                return false
            }
        })
    }

    private fun inflateRecyclerView(procedureModelDbList: List<ProcedureModelDb>) {
        // create adapter
        proceduresRVAdapter = ProceduresRvAdapter(
            proceduresList = procedureModelDbList
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchProceduresRV?.layoutManager = layoutManager
        searchProceduresRV?.adapter = proceduresRVAdapter

        // set clickListener on clientsRV
        proceduresRVAdapter?.setOnItemClickListener(object :
            ProceduresRvAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // client selected
                val procedureModelDb = ProcedureModelDb(
                    _id = procedureModelDbList[position]._id,
                    procedureName = procedureModelDbList[position].procedureName,
                    procedurePrice = procedureModelDbList[position].procedurePrice,
                    procedureNotes = procedureModelDbList[position].procedureNotes
                )

                findNavController().currentBackStackEntry?.savedStateHandle?.set("procedure", procedureModelDb)
                dismiss() // закрыть диалоговое окно после передачи данных
            }
        })
    }

    private fun clearSearchView() {
        searchView?.setQuery(null, true)
    }
}