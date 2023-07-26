package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.DateWeekAppModel
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.FullMonthViewViewModel
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv.FullMonthChildAdapter
import com.example.projectnailsschedule.util.Util
import java.time.LocalDate

class FullMonthViewRVAdapter(
    private val list: MutableList<DateWeekAppModel>
) : RecyclerView.Adapter<FullMonthViewRVViewHolder>(
) {
    val log = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullMonthViewRVViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View =
            inflater.inflate(R.layout.appointments_month_view_rv_parent_item, parent, false)
        return FullMonthViewRVViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FullMonthViewRVViewHolder, position: Int) {

        val parentItem = list[position]
        holder.date.text = list[position].day
        holder.weekDayName.text = list[position].weekDay

        if (list[position].appointmentsList.isEmpty()) {
            holder.noAppointmentsText.visibility = View.VISIBLE
            holder.childRv.visibility = View.GONE
        } else {
            holder.noAppointmentsText.visibility = View.GONE
            holder.childRv.visibility = View.VISIBLE

            holder.childRv.setHasFixedSize(true)
            holder.childRv.layoutManager = GridLayoutManager(holder.itemView.context, 1)

            val fullMonthChildAdapter = FullMonthChildAdapter(parentItem.appointmentsList)
            holder.childRv.adapter = fullMonthChildAdapter
        }
    }
}