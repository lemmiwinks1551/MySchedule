package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel
import java.time.LocalDate

internal class CalendarAdapter(
    private val daysInMonth: ArrayList<String>,
    private val onItemListener: CalendarFragment,
    private val calendarViewModel: CalendarViewModel,
    private val selectedDate: LocalDate
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var log = this::class.simpleName
    private val busyDay = "Занят"
    private val semiBusyDay = "Есть записи"
    private val notBusyDay = "Выходной"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)

        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        // get day to work with
        val dayInHolder = daysInMonth[position]

        // set bold font for current day
        val nowDate = LocalDate.now()
        if (nowDate.month == selectedDate.month &&
            nowDate.dayOfMonth.toString() == dayInHolder
        ) {
            holder.dayOfMonth.setTypeface(null, Typeface.BOLD)
            holder.dayOfMonth.textSize = 23f
        }

        // set day number in CalendarViewHolder (even if it`s empty)
        holder.dayOfMonth.text = dayInHolder

        // set background for days with special statuses
        if (dayInHolder != "") {
            var dateParams = DateParams(
                _id = null,
                date = LocalDate.now(),
                status = null
            )

            // get day status from Data
            dateParams = calendarViewModel.getDayStatus(dateParams)

            when (dateParams.status) {
                semiBusyDay -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_medium)
                busyDay -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_busy)
                notBusyDay -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_day_off)
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