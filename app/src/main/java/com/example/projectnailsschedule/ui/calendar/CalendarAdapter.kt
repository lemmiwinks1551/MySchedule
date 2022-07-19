package com.example.projectnailsschedule.ui.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import java.time.LocalDate

internal class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: CalendarFragment
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)

        // Выравнивает элементы по высоте
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.11).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        // выполняет привязку объекта ViewHolder к объекту по определенной позиции.
        // Если день имесяц для отправки в холдер текущие - покрасить ячейку
        val nowDate = LocalDate.now()
        val dayInHolder = daysOfMonth[position]
        if (dayInHolder == nowDate.dayOfMonth.toString() && month == 0) {
            holder.dayOfMonth.setBackgroundColor(Color.RED)
        }
        holder.dayOfMonth.text = daysOfMonth[position]
        // TODO: 13.07.2022 Добавить логику выбора из БД и раскраску дней
    }

    override fun getItemCount(): Int {
        // возвращает количество объектов в списке
        return daysOfMonth.size
    }

    interface OnItemListener {
        // Подключаем интерфейс onItemListener
        fun onItemClick(position: Int, dayText: String?)
    }

    companion object {
        // Адаптер работает с ViewHolder`ом
        var month = 0
    }
}