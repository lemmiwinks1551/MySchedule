package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel

internal class CalendarAdapter(
    private val daysInMonth: ArrayList<String>,
    private val onItemListener: CalendarFragment,
    private val calendarViewModel: CalendarViewModel
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var log = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_recycler_view_cell, parent, false)

        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        // get day to work with
        val dayInHolder = daysInMonth[position]

        // set day number in CalendarViewHolder (even if it`s empty)
        holder.date.text = dayInHolder

        // set appointments count
        if (dayInHolder != "") {
            try {
                // try to get appointment count from date
                // can throw exception if day is`t exists in month
                val appointmentCount: Int
                val dateParams = DateParams(
                    date = calendarViewModel.selectedDateParams.value?.date?.withDayOfMonth(dayInHolder.toInt())
                )
                appointmentCount =
                    calendarViewModel.getCursorAppointments(dateParams = dateParams).count

                if (appointmentCount > 0) {
                    // if appointments exists
                    holder.dateAppointmentsCount.text = appointmentCount.toString()
                }
            } catch (e: Exception) {
                Log.e(log, e.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}