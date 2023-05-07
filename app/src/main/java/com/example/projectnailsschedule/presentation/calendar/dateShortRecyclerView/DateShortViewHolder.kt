package com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment

class DateShortViewHolder internal constructor(itemView: View, onItemListener: CalendarFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val clientName: TextView
    val time: TextView
    val procedure: TextView
    private val onItemListener: DateShortAdapter.OnItemListener

    init {
        clientName = itemView.findViewById(R.id.appointment_name_short)
        time = itemView.findViewById(R.id.appointment_start_short)
        procedure = itemView.findViewById(R.id.appointment_procedure_short)
        itemView.setOnClickListener(this)
        this.onItemListener = onItemListener
    }

    override fun onClick(p0: View?) {
        onItemListener.onItemClickShortDate()
    }
}