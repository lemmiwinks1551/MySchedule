package com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment

internal class DateShortAdapter(
    private val selectedDayParams: DateParams,
    private val calendarFragment: CalendarFragment
) :
    RecyclerView.Adapter<DateShortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateShortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.database_short_rv_item, parent, false)
        return DateShortViewHolder(view, calendarFragment)
    }

    override fun onBindViewHolder(holder: DateShortViewHolder, position: Int) {
        with(selectedDayParams.appointmentsList?.get(position)!!) {
            holder.number.text = String.format("${position + 1}.")
            holder.clientName.text = name
            holder.time.text = time
            holder.procedure.text = procedure
            holder.notes.text = notes
        }
    }

    override fun getItemCount(): Int {
        return selectedDayParams.appointmentsList?.size!!
    }

    interface OnItemListener {
        fun onItemClickShortDate()
    }
}