package com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
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
            inflateView(name, holder.clientName)
            inflateView(time, holder.time)
            inflateView(procedure, holder.procedure)
            inflateView(notes, holder.notes)
            holder.photo.setImageURI(photo?.toUri())
        }
    }

    private fun inflateView(string: String?, textView: TextView) {
        if (string.isNullOrEmpty()) {
            textView.text = "-"
            textView.gravity = Gravity.CENTER
        } else {
            textView.text = string
        }
    }

    override fun getItemCount(): Int {
        return selectedDayParams.appointmentsList?.size!!
    }

    interface OnItemListener {
        fun onItemClickShortDate()
    }
}