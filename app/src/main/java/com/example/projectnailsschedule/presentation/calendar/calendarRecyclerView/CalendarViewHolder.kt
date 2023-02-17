package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment


class CalendarViewHolder internal constructor(itemView: View, onItemListener: CalendarFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    // ViewHolder - класс, содержащий ссылки на все View, которые находятся в одном элементе RecyclerView элементы
    // Через ViewHolder заполняется RecyclerVIew

    val date: TextView
    val dateAppointmentsCount: TextView
    private val onItemListener: CalendarAdapter.OnItemListener

    init {
        date = itemView.findViewById(R.id.date_cell)
        dateAppointmentsCount = itemView.findViewById(R.id.date_appointments)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, date.text as String)
    }
}
