package com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class DateShortViewHolder internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val clientName: TextView
    val starTime: TextView
    val procedure: TextView

    init {
        clientName = itemView.findViewById(R.id.appointment_name_short)
        starTime = itemView.findViewById(R.id.appointment_start_short)
        procedure = itemView.findViewById(R.id.appointment_procedure_short)
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

    }
}