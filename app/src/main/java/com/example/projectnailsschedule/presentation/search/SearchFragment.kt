package com.example.projectnailsschedule.presentation.search

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.searchRecyclerVIew.SearchRvAdapter
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val log = this::class.simpleName
    private val searchViewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchTextView: SearchView? = null
    private var searchRecyclerView: RecyclerView? = null
    private var appointmentCount: TextView? = null
    private val bindingKeyAppointment = "appointmentParams"

    private var appointmentList: List<AppointmentModelDb>? = null
    private var searchRvAdapter: SearchRvAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init observers
        initObservers()

        // init click listeners
        initClickListeners()

        swipeToDelete()

        return binding.root
    }

    private fun initWidgets() {
        searchTextView = binding.searchView
        searchRecyclerView = binding.searchRecyclerView
        appointmentCount = binding.appointmentsCountTextView

    }

    private fun initObservers() {
        searchViewModel.appointmentsTotalCount.observe(viewLifecycleOwner) {
            appointmentCount?.text = getString(R.string.appointments_count, it)
        }
    }

    private fun initClickListeners() {
        searchTextView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search only after button Search pressed on keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = "%$newText%"
                searchViewModel!!.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
                    inflateSearchRecyclerVIew(list)
                    appointmentList = list
                }
                return false
            }
        })
    }

    private fun inflateSearchRecyclerVIew(appointmentsList: List<AppointmentModelDb>) {
        // create adapter
        searchRvAdapter = SearchRvAdapter(
            appointmentCount = appointmentsList.size,
            appointmentsList = appointmentsList,
            searchViewModel = searchViewModel
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        searchRecyclerView?.layoutManager = layoutManager
        searchRecyclerView?.adapter = searchRvAdapter

        searchRvAdapter!!.setOnItemClickListener(object : SearchRvAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected appointment
                val bundle = Bundle()
                val appointmentModelDb = AppointmentModelDb(
                    _id = appointmentList?.get(position)?._id,
                    date = appointmentList?.get(position)?.date,
                    name = appointmentList?.get(position)?.name,
                    time = appointmentList?.get(position)?.time,
                    procedure = appointmentList?.get(position)?.procedure,
                    phone = appointmentList?.get(position)?.phone,
                    telegram = appointmentList?.get(position)?.telegram,
                    instagram = appointmentList?.get(position)?.instagram,
                    vk = appointmentList?.get(position)?.vk,
                    whatsapp = appointmentList?.get(position)?.whatsapp,
                    notes = appointmentList?.get(position)?.notes,
                    deleted = appointmentList?.get(position)!!.deleted
                )
                val navController = findNavController()
                bundle.putParcelable(bindingKeyAppointment, appointmentModelDb)
                navController.navigate(R.id.action_nav_search_to_nav_appointment, bundle)
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
                val deleteAppointmentModelDb: AppointmentModelDb =
                    appointmentList!![viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition

                // delete client from Db
                searchViewModel.deleteAppointment(deleteAppointmentModelDb)

                searchRvAdapter?.notifyItemRemoved(position)

                // show Snackbar
                Snackbar.make(
                    searchRecyclerView!!,
                    getString(R.string.deleted_appointment_text, deleteAppointmentModelDb.name),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(resources.getColor(R.color.yellow))
                    .setActionTextColor(resources.getColor(R.color.black))
                    .setTextColor(resources.getColor(R.color.black))
                    .setAction(
                        getString(R.string.cancel)
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        searchViewModel.saveAppointment(deleteAppointmentModelDb)

                        // below line is to notify item is
                        // added to our adapter class.
                        searchRvAdapter?.notifyDataSetChanged()
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

                val colorDrawableBackground = ColorDrawable(Color.parseColor("#ffcce6"))

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
        }).attachToRecyclerView(searchRecyclerView)
    }

    override fun onResume() {
        searchTextView?.setQuery("", true) // clear search bar
        super.onResume()
        RuStoreAd().banner(requireContext(), binding.root)
    }

    override fun onPause() {
        searchTextView?.setQuery("", true) // clear search bar
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}