package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.SearchFragment
import com.example.projectnailsschedule.presentation.search.SearchViewModel

internal class SearchAdapter(
    private var appointmentCount: Int,
    private val searchFragment: SearchFragment,
    private var appointmentsList: List<AppointmentModelDb>,
    private val searchViewModel: SearchViewModel
) :
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.search_recycler_view_item, parent, false)

        return SearchViewHolder(view, searchFragment)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val appointmentModelDb = appointmentsList[position]

        // declare current day appointments
        val dateAppointment = appointmentsList[position]
        holder.appointmentModelDb = dateAppointment
        holder.position = position

        // get date appointments
        with(holder) {
            this.appointmentModelDb = appointmentModelDb
            appointmentId = appointmentsList[position]._id
            appointmentDate = appointmentModelDb.date

            date.text = appointmentModelDb.date
            time.text = appointmentModelDb.time
            procedure.text = appointmentModelDb.procedure
            name.text = appointmentModelDb.name
            phone.text = appointmentModelDb.phone
            notes.text = appointmentModelDb.notes
        }
    }

    override fun getItemCount(): Int {
        // set current appointment count
        searchViewModel.appointmentCount.value = appointmentCount
        searchViewModel.getAllAppointments()
        return appointmentCount
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}