package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class FullMonthViewRVViewHolder internal constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val date: TextView
    val weekDayName: TextView
    val childRv: RecyclerView

    init {
        date = itemView.findViewById(R.id.date_text_view)
        weekDayName = itemView.findViewById(R.id.week_day_name_text_view)
        childRv = itemView.findViewById(R.id.child_rv)
    }
}