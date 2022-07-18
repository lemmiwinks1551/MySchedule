package com.example.projectnailsschedule.ui.calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R


class CalendarViewHolder internal constructor(itemView: View, onItemListener: CalendarFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    // ViewHolder - класс, содержащий ссылки на все View, которые находятся в одном элементе RecyclerView элементы
    // Через ViewHolder заполняется RecyclerVIew
    // TODO: 13.07.2022 Сделать через binding
    val dayOfMonth: TextView
    private val onItemListener: CalendarAdapter.OnItemListener
    override fun onClick(view: View) {
        // Вызывает onItemClick класса MainActivity
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }

    init {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }
}
