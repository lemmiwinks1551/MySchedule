package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel
import com.example.projectnailsschedule.util.Util
import java.time.LocalDate

internal class CalendarAdapter(
    private val daysInMonth: ArrayList<String>,
    private val calendarFragment: CalendarFragment,
    private val calendarViewModel: CalendarViewModel
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var log = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_rv_item, parent, false)

        return CalendarViewHolder(view, calendarFragment)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        // get day to work with
        val dayInHolder = daysInMonth[position]
        // set day number in CalendarViewHolder (even if it`s empty)
        holder.date.text = dayInHolder

        // set appointments count
        if (dayInHolder != "") {
            // get appointment count from date
            val appointmentCount: Int
            val dateParams = DateParams(
                date = calendarViewModel.selectedDate.value?.date?.withDayOfMonth(
                    dayInHolder.toInt()
                )
            )

            // get date appointment count
            appointmentCount =
                calendarViewModel.getArrayAppointments(dateParams = dateParams).size

            Log.e(log, "$dateParams")

            // if appointments exists
            if (appointmentCount > 0) {
                holder.dateAppointmentsCount.text = appointmentCount.toString()
            }

            // if day is today set custom frame
            if (dateParams.date!! == LocalDate.now()) {
                holder.date.setTextColor(Color.RED)
            }
        }
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }

    interface OnItemListener {
        fun onCalendarClick(position: Int, dayText: String?)
    }
}