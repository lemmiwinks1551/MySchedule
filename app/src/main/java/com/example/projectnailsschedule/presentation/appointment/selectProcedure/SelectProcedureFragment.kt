package com.example.projectnailsschedule.presentation.appointment.selectProcedure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSelectProcedureBinding
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.presentation.appointment.selectProcedure.selectProcedureRV.SelectProcedureRVAdapter

class SelectProcedureFragment : DialogFragment() {
    val log = this::class.simpleName

    private var selectProcedureViewModel: SelectProcedureViewModel? = null

    private var _binding: FragmentSelectProcedureBinding? = null
    private var procedureSelectRv: RecyclerView? = null
    private var proceduresSv: SearchView? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        selectProcedureViewModel = ViewModelProvider(
            this,
            SelectProcedureViewModelFactory(context)
        )[SelectProcedureViewModel::class.java]

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
        _binding = FragmentSelectProcedureBinding.inflate(inflater, container, false)

        initWidgets()

        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        procedureSelectRv = binding.proceduresRecyclerViewSelectProcedure
        proceduresSv = binding.selectProcedureSearchViewSelectProcedure
    }

    private fun initClickListeners() {
        binding.selectProcedureSearchViewSelectProcedure.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                selectProcedureViewModel!!.searchProcedures(searchQuery)
                    .observe(viewLifecycleOwner) { list ->
                        inflateSearchRecyclerVIew(list)
                    }
                return false
            }
        })
    }

    private fun inflateSearchRecyclerVIew(procedureModelDbList: List<ProcedureModelDb>) {
        // create adapter
        val selectClientRVAdapter = SelectProcedureRVAdapter(
            proceduresCount = procedureModelDbList.size,
            proceduresList = procedureModelDbList
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        procedureSelectRv?.layoutManager = layoutManager
        procedureSelectRv?.adapter = selectClientRVAdapter

        // set clickListener on clientsRV
        selectClientRVAdapter.setOnItemClickListener(object :
            SelectProcedureRVAdapter.OnItemClickListener {
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

    override fun onResume() {
        proceduresSv?.setQuery(null, true) // clear search bar
        super.onResume()
    }

    override fun onPause() {
        proceduresSv?.setQuery(null, true) // clear search bar
        super.onPause()
    }
}