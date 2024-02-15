package com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DateShortAdapter(
    private val appointmentsCount: Int,
    private val selectedDayParams: DateParams,
    private val calendarViewModel: CalendarViewModel,
    private val calendarFragment: CalendarFragment
) :
    RecyclerView.Adapter<DateShortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateShortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.database_short_rv_item, parent, false)
        return DateShortViewHolder(view, calendarFragment)
    }

    override fun onBindViewHolder(holder: DateShortViewHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val selectedDayParams = calendarViewModel.getArrayAppointments(selectedDayParams)
            val currentDayAppointments = selectedDayParams[position]

            withContext(Dispatchers.Main) {
                holder.clientName.text = currentDayAppointments.name
                holder.time.text = currentDayAppointments.time
                holder.procedure.text = currentDayAppointments.procedure
                holder.number.text = String.format("${position + 1}.")
                holder.notes.text = currentDayAppointments.notes
            }
        }
    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }

    interface OnItemListener {
        fun onItemClickShortDate()
    }
}