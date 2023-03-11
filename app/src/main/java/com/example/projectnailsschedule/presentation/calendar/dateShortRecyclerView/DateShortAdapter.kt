package com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel

internal class DateShortAdapter(
    private val appointmentsCount: Int,
    private val selectedDayParams: DateParams,
    private val calendarViewModel: CalendarViewModel
) :
    RecyclerView.Adapter<DateShortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateShortViewHolder {
        // Set ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.database_short_recycler_view, parent, false)
        return DateShortViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateShortViewHolder, position: Int) {

        val cursor = calendarViewModel.getCursorAppointments(selectedDayParams)
        cursor.moveToFirst()
        if (cursor.moveToPosition(position)) {
            // Set time in holder
            holder.starTime.text = cursor.getString(2)

            // Set name in holder
            holder.clientName.text = cursor.getString(4)

            // Set procedure in holder
            holder.procedure.text = cursor.getString(3)
        }
    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }
}