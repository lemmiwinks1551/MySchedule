package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel
import java.time.LocalDate

class CalendarRvAdapter(
    private val daysInMonth: ArrayList<String>,
    private val calendarFragment: CalendarFragment,
    private val calendarViewModel: CalendarViewModel
) : RecyclerView.Adapter<CalendarRvAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var date: TextView
        lateinit var dateAppointmentsCount: TextView
        lateinit var cellLayout: ConstraintLayout

        init {
            inflateViews()
            setClickListeners(listener)
        }

        private fun inflateViews() {
            date = itemView.findViewById(R.id.date_cell)
            dateAppointmentsCount = itemView.findViewById(R.id.date_appointments_text_view)
            cellLayout = itemView.findViewById(R.id.calendarRecyclerViewCell)
        }

        private fun setClickListeners(listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private val log = this::class.simpleName

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_rv_item, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get day to work with
        val dayInHolder = daysInMonth[position]

        // set day number in CalendarViewHolder
        holder.date.text = dayInHolder

        // set appointments count
        if (dayInHolder != "") {
            // get appointment count from date
            val appointmentCount: Int
            val dateParams = DateParams(
                date = calendarViewModel.localSelectedDate.value?.date?.withDayOfMonth(
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

    private fun inflateViews() {
    }
}