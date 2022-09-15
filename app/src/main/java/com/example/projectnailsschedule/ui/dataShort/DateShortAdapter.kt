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

        val map = dateShorGetDbData.getTimeNameMap() // получаем словарь
        val namesArray:Array<String> = map.keys.toTypedArray() // создаем массив ключей

        val name = namesArray[0]    // Получаем имя, ищем в словаре 0 элемент массива
        val startTime = map[name]   // Получаем время, ищем по имени в Map

        holder.clientName.text = name     // Устанавливаем в holder Имя
        holder.starTime.text = startTime  // Устанавилваем в holder Время

        map.remove(name)    // Удаляем из Map элемент с данным именем
    }

    interface OnItemListener {
        // Подключаем интерфейс onItemListener
        fun onItemClick(position: Int, dayText: String?)
    }

    override fun getItemCount(): Int {
        return rowsCount

    }
}