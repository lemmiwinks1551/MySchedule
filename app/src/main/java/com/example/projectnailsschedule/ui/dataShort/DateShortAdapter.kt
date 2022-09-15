package com.example.projectnailsschedule.ui.dataShort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.ui.calendar.CalendarFragment

internal class DateShortAdapter(
    private val rowsCount: Int,
    private val onItemListener: CalendarFragment,
    private val dateShorGetDbData: DateShorGetDbData
) :
    RecyclerView.Adapter<DateShortViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateShortViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.database_short_view, parent, false)
        return DateShortViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: DateShortViewHolder, position: Int) {
        // Получаем соответствие Клиент-Имя
        // Устанавливаем их в holder
        val map = dateShorGetDbData.getTimeNameMap()
        val name = map
        val startTime = map

        holder.starTime.text = "startTime"
        holder.clientName.text = "name"

    }

    interface OnItemListener {
        // Подключаем интерфейс onItemListener
        fun onItemClick(position: Int, dayText: String?)
    }

    override fun getItemCount(): Int {
        return rowsCount

    }
}