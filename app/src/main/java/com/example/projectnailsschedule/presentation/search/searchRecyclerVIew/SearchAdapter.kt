package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.presentation.search.SearchFragment

internal class SearchAdapter(
    private val appointmentCount: Int,
    private val searchFragment: SearchFragment,
    private var appointmentsList: MutableList<AppointmentParams>
) :
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.search_recycler_view_item, parent, false)

        return SearchViewHolder(view, searchFragment)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.date.text = appointmentsList[position].appointmentDate.toString()
        holder.name.text = appointmentsList[position].clientName.toString()
        holder.phone.text = appointmentsList[position].phoneNum.toString()
        holder.time.text = appointmentsList[position].startTime.toString()
        holder.misc.text = appointmentsList[position].misc.toString()
    }

    override fun getItemCount(): Int {
        return appointmentCount
    }

    // method for filtering our recyclerview items.
/*    fun filterList(filterlist: ArrayList<AppointmentParams>) {
        // below line is to add our filtered
        // list in our course array list.
        appointmentsList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }*/

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}