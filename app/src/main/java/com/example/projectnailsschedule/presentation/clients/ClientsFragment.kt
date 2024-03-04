package com.example.projectnailsschedule.presentation.clients

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentClientsBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ClientsFragment : Fragment() {
    private val clientsViewModel: ClientsViewModel by activityViewModels()

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!

    private var clientsList: MutableList<ClientModelDb>? = null
    private var clientsRVAdapter: ClientsRv? = null

    private var searchView: SearchView? = null
    private var searchClientsRV: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)

        initViews()

        swipeToDelete()

        initClickListeners()

        clearSearchView()

        return binding.root
    }

    private fun initViews() {
        searchView = binding.clientsSearchView
        searchClientsRV = binding.clientsRecyclerView
        addButton = binding.fragmentClientsAddButton
    }

    private fun initClickListeners() {
        // search panel listener
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
                        inflateClientsRecyclerView(clientsList!!)
                    }
                }
                return false
            }
        })

        val searchCloseButtonId =
            searchView?.findViewById<View>(androidx.appcompat.R.id.search_close_btn)?.id
        if (searchCloseButtonId != null) {
            searchView?.findViewById<ImageView>(searchCloseButtonId)?.setOnClickListener {
                clearSearchView()
                snackbar?.dismiss()
            }
        }

        // add new client
        binding.fragmentClientsAddButton.setOnClickListener {
            binding.fragmentClientsAddButton.findNavController().navigate(
                R.id.action_nav_clients_to_nav_client_edit_fragment
            )
        }
    }

    private fun inflateClientsRecyclerView(clientsList: List<ClientModelDb>) {
        // create adapter
        clientsRVAdapter = ClientsRv(
            clientsList = clientsList,
            clientsViewModel = clientsViewModel
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchClientsRV?.layoutManager = layoutManager
        searchClientsRV?.adapter = clientsRVAdapter

        // set clickListener on clientsRV
        clientsRVAdapter?.setOnItemClickListener(object : ClientsRv.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected client
                clientsViewModel.selectedClient = ClientModelDb(
                    _id = clientsList[position]._id,
                    name = clientsList[position].name,
                    phone = clientsList[position].phone,
                    vk = clientsList[position].vk,
                    telegram = clientsList[position].telegram,
                    instagram = clientsList[position].instagram,
                    whatsapp = clientsList[position].whatsapp,
                    notes = clientsList[position].notes
                )
                findNavController().navigate(R.id.action_nav_clients_to_nav_client_edit_fragment)
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
                val position = viewHolder.adapterPosition
                val deleteClientModelDb: ClientModelDb = clientsList!![viewHolder.adapterPosition]

                // delete client from Db
                lifecycleScope.launch(Dispatchers.IO) {
                    clientsViewModel.deleteClient(deleteClientModelDb)
                    withContext(Dispatchers.Main) {
                        clientsList!!.removeAt(position)
                        clientsRVAdapter?.notifyItemRemoved(position)
                    }
                }

                // show Snackbar
                snackbar = Snackbar.make(
                    searchClientsRV!!,
                    requireContext().getString(
                        R.string.deleted_client_text,
                        deleteClientModelDb.name
                    ),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(resources.getColor(R.color.yellow))
                    .setActionTextColor(resources.getColor(R.color.black))
                    .setTextColor(resources.getColor(R.color.black))
                    .setAction(
                        getString(R.string.cancel)
                    ) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            clientsViewModel.insertClient(deleteClientModelDb)
                            withContext(Dispatchers.Main) {
                                clientsList!!.add(position, deleteClientModelDb)
                                clientsRVAdapter?.notifyItemInserted(position)
                            }
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
        }).attachToRecyclerView(searchClientsRV)
    }

    private fun clearSearchView() {
        searchView?.setQuery(null, true)
    }

    override fun onResume() {
        super.onResume()
        RuStoreAd().banner(requireContext(), binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        _binding = null
    }
}