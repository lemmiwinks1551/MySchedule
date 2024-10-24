package com.example.projectnailsschedule.presentation.calendar.listMonthView.fullMonthViewRV

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FullMonthViewRVViewHolder internal constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val date: TextView
    val weekDayName: TextView
    val childRv: RecyclerView
    val addAppointmentFab: FloatingActionButton
    val dateCl: ConstraintLayout
    val mothViewParentCl: ConstraintLayout
    val selectedBackground: ImageView

    init {
        date = itemView.findViewById(R.id.date_text_view)
        weekDayName = itemView.findViewById(R.id.week_day_name_text_view)
        childRv = itemView.findViewById(R.id.child_rv)
        addAppointmentFab = itemView.findViewById(R.id.month_view_add_appointment_fab)
        dateCl = itemView.findViewById(R.id.date_cl)
        mothViewParentCl = itemView.findViewById(R.id.moth_view_parent_cl)
        selectedBackground = itemView.findViewById(R.id.selected_background)
    }
}