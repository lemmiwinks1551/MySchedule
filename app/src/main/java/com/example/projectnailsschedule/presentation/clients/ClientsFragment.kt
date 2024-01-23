package com.example.projectnailsschedule.presentation.clients

import android.graphics.Canvas
import android.graphics.Color
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentClientsBinding
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsAdapter
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientsFragment : Fragment() {
    val log = this::class.simpleName

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private val clientsViewModel: ClientsViewModel by viewModels()

    private var clientsList: List<ClientModelDb>? = null
    private var clientsRVAdapter: ClientsAdapter? = null

    private var clientsSearchView: SearchView? = null
    private var searchClientsRV: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var clientsCountTextView: TextView? = null

    private var bindingKeyClientEdit = "editClient"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentClientsBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // swipe to delete
        swipeToDelete()

        // initClickListeners
        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        clientsSearchView = binding.clientsSearchView
        searchClientsRV = binding.clientsRecyclerView
        addButton = binding.fragmentClientsAddButton
        clientsCountTextView = binding.clientsCountTextView
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
                clientsViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
                    clientsList = list
                    inflateClientsRecyclerView(list)
                }
                return false
            }
        })

        // add new client
        binding.fragmentClientsAddButton.setOnClickListener {
            binding.fragmentClientsAddButton.findNavController().navigate(
                R.id.action_nav_clients_to_nav_client_edit_fragment
            )
        }
    }

    private fun inflateClientsRecyclerView(clientsList: List<ClientModelDb>) {
        // create adapter
        clientsRVAdapter = ClientsAdapter(
            clientsCount = clientsList.size,
            clientsList = clientsList,
            clientsViewModel = clientsViewModel
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchClientsRV?.layoutManager = layoutManager
        searchClientsRV?.adapter = clientsRVAdapter

        // set clickListener on clientsRV
        clientsRVAdapter?.setOnItemClickListener(object : ClientsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected client
                val bundle = Bundle()
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
                val navController = findNavController()
                bundle.putParcelable(bindingKeyClientEdit, clientModelDb)
                navController.navigate(R.id.action_nav_clients_to_nav_client_edit_fragment, bundle)
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
                // this method is called when we swipe our item to left direction.
                // on below line we are getting the item at a particular position.
                val deleteClientModelDb: ClientModelDb = clientsList!![viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition

                // delete client from Db
                clientsViewModel.deleteClient(deleteClientModelDb)

                clientsRVAdapter?.notifyItemRemoved(position)
                clientsSearchView?.setQuery(null, true) // clear search bar

                // show Snackbar
                Snackbar.make(
                    searchClientsRV!!,
                    requireContext().getString(R.string.deleted_client_text, deleteClientModelDb.name),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(resources.getColor(R.color.yellow))
                    .setActionTextColor(resources.getColor(R.color.black))
                    .setTextColor(resources.getColor(R.color.black))
                    .setAction(
                        getString(R.string.cancel)
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        clientsViewModel.saveClient(deleteClientModelDb)

                        // below line is to notify item is
                        // added to our adapter class.

                        clientsRVAdapter?.notifyDataSetChanged()
                    }.show()

            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return super.getSwipeEscapeVelocity(defaultValue) * 10
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val deleteIcon: Drawable? =
                    ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)!!

                val itemView = viewHolder.itemView
                val iconMarginVertical = (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2

                val colorDrawableBackground = ColorDrawable(resources.getColor(R.color.yellow))

                val left = itemView.right - deleteIcon.intrinsicWidth - deleteIcon.intrinsicWidth // 882
                val right = itemView.right - deleteIcon.intrinsicWidth // 1014
                val top = itemView.top + iconMarginVertical
                val bottom = itemView.bottom - iconMarginVertical

                colorDrawableBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                deleteIcon.setBounds(left, top, right, bottom)

                deleteIcon.level = 0

                colorDrawableBackground.draw(c)
                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(searchClientsRV)
    }

    override fun onResume() {
        clientsSearchView?.setQuery(null, true) // clear search bar
        super.onResume()
        RuStoreAd().banner(requireContext(), binding.root)
    }

    override fun onPause() {
        clientsSearchView?.setQuery(null, true) // clear search bar
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}