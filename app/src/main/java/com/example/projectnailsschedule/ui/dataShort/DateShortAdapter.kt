package com.example.projectnailsschedule.ui.dataShort

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.ui.calendar.CalendarFragment
import com.example.projectnailsschedule.ui.calendar.CalendarViewHolder

class DateShortAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: CalendarFragment,
    private val dayStatuses: MutableMap<String, String>
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}