package com.example.projectnailsschedule.ui.dataShort

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.ui.calendar.CalendarFragment

class DateShortViewHolder internal constructor(itemView: View, onItemListener: CalendarFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val clientName: TextView
    val starTime: TextView


    init {
        clientName = itemView.findViewById(R.id.appointment_name_short)
        starTime = itemView.findViewById(R.id.appointment_start_short)

        itemView.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}