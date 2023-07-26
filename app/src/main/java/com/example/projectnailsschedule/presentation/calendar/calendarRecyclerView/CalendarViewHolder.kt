package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment

class CalendarViewHolder internal constructor(itemView: View, onItemListener: CalendarFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val date: TextView
    val dateAppointmentsCount: TextView
    private val onItemListener: CalendarAdapter.OnItemListener
    val cellLayout: ConstraintLayout

    init {
        date = itemView.findViewById(R.id.date_cell)
        dateAppointmentsCount = itemView.findViewById(R.id.date_appointments_text_view)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
        cellLayout = itemView.findViewById(R.id.calendarRecyclerViewCell)
    }

    override fun onClick(view: View) {
        onItemListener.onCalendarClick(adapterPosition, date.text as String)
    }
}
