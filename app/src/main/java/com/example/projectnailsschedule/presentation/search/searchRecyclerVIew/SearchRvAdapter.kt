package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.SearchViewModel

internal class SearchRvAdapter(
    private var appointmentCount: Int,
    private var appointmentsList: List<AppointmentModelDb>,
    private val searchViewModel: SearchViewModel,
    private val context: Context
) :
    RecyclerView.Adapter<SearchViewHolder>()
{
    val log = this::class.simpleName

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.search_recycler_view_item, parent, false)

        return SearchViewHolder(view, mListener)
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

        holder.callClientButton.setOnClickListener {
            val phone = appointmentsList[position].phone.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // set current appointment count
        searchViewModel.appointmentsTotalCount.value = appointmentCount
        searchViewModel.getAllAppointmentsLiveData()
        return appointmentCount
    }
}