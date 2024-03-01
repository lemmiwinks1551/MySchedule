package com.example.projectnailsschedule.presentation.procedures

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentProceduresBinding
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProceduresFragment : Fragment() {
    private val proceduresViewModel: ProceduresViewModel by activityViewModels()

    private var _binding: FragmentProceduresBinding? = null
    private val binding get() = _binding!!

    private var proceduresList: List<ProcedureModelDb>? = null
    private var proceduresRVAdapter: ProceduresRv? = null

    private var proceduresSearchView: SearchView? = null
    private var searchProceduresRV: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var proceduresCountTextView: TextView? = null
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProceduresBinding.inflate(inflater, container, false)

        initViews()

        swipeToDelete()

        initClickListeners()

        // init inflate recycler view
        proceduresSearchView?.setQuery(null, true)

        return binding.root
    }

    private fun initViews() {
        proceduresSearchView = binding.proceduresSearchView
        searchProceduresRV = binding.proceduresRecyclerView
        addButton = binding.fragmentProceduresAddButton
        proceduresCountTextView = binding.proceduresCountTextView
    }

    private fun initClickListeners() {
        // search panel listener
        proceduresSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                lifecycleScope.launch {
                    proceduresViewModel.searchProcedure(searchQuery)
                        .observe(viewLifecycleOwner) { list ->
                            proceduresList = list
                            inflateClientsRecyclerView(list)
                        }
                }
                return false
            }
        })

        // add new procedure
        binding.fragmentProceduresAddButton.setOnClickListener {
            binding.fragmentProceduresAddButton.findNavController().navigate(
                R.id.action_nav_procedures_to_nav_procedure_edit
            )
        }
    }

    private fun inflateClientsRecyclerView(procedureModelDbList: List<ProcedureModelDb>) {
        // create adapter
        proceduresRVAdapter = ProceduresRv(
            proceduresList = procedureModelDbList
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchProceduresRV?.layoutManager = layoutManager
        searchProceduresRV?.adapter = proceduresRVAdapter

        // set clickListener on proceduresRV
        proceduresRVAdapter?.setOnItemClickListener(object : ProceduresRv.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected procedure
                proceduresViewModel.selectedProcedure = ProcedureModelDb(
                    _id = procedureModelDbList[position]._id,
                    procedureName = procedureModelDbList[position].procedureName,
                    procedurePrice = procedureModelDbList[position].procedurePrice,
                    procedureNotes = procedureModelDbList[position].procedureNotes
                )
                findNavController().navigate(R.id.action_nav_procedures_to_nav_procedure_edit)
            }
        })
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteProcedureModelDb: ProcedureModelDb =
                    proceduresList!![viewHolder.adapterPosition]

                // delete client from Db
                lifecycleScope.launch {
                    proceduresViewModel.deleteProcedure(deleteProcedureModelDb)
                    clearSearchBar()
                }

                // show Snackbar
                snackbar = Snackbar.make(
                    searchProceduresRV!!,
                    getString(R.string.procedure_deleted, deleteProcedureModelDb.procedureName),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(resources.getColor(R.color.yellow))
                    .setActionTextColor(resources.getColor(R.color.black))
                    .setTextColor(resources.getColor(R.color.black))
                    .setAction(
                        getString(R.string.cancel)
                    ) {
                        lifecycleScope.launch {
                            proceduresViewModel.insertProcedure(deleteProcedureModelDb)
                            clearSearchBar()
                        }
                    }
                snackbar!!.show()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return super.getSwipeEscapeVelocity(defaultValue) * 10
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val deleteIcon: Drawable? =
                    ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)!!

                val itemView = viewHolder.itemView
                val iconMarginVertical =
                    (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2

                val colorDrawableBackground = ColorDrawable(resources.getColor(R.color.yellow))

                val left =
                    itemView.right - deleteIcon.intrinsicWidth - deleteIcon.intrinsicWidth // 882
                val right = itemView.right - deleteIcon.intrinsicWidth // 1014
                val top = itemView.top + iconMarginVertical
                val bottom = itemView.bottom - iconMarginVertical

                colorDrawableBackground.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                deleteIcon.setBounds(left, top, right, bottom)

                deleteIcon.level = 0

                colorDrawableBackground.draw(c)
                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )

                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(searchProceduresRV)
    }

    override fun onResume() {
        super.onResume()
        RuStoreAd().banner(requireContext(), binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        snackbar?.dismiss()
    }

    private fun clearSearchBar() {
        proceduresSearchView?.setQuery(null, true) // clear search bar
    }
}