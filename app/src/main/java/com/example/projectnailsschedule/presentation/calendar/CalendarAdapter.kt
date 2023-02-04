package com.example.projectnailsschedule.presentation.calendar

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import java.time.LocalDate

internal class CalendarAdapter(
    private val daysInMonth: ArrayList<String>,
    private val onItemListener: CalendarFragment,
    private val calendarViewModel: CalendarViewModel,
    private val selectedDate: LocalDate
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var log = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)

        // Выравнивает элементы по высоте
        val layoutParams = view.layoutParams

        //layoutParams.height = (parent.height * 0.2).toInt()

        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        // выполняет привязку объекта ViewHolder к объекту по определенной позиции.
        // Если день и месяц для отправки в холдер текущие - покрасить ячейку
        val nowDate = LocalDate.now()
        val dayInHolder = daysInMonth[position]

        //Устанавливаем фон для сегодняшнего дня
        if (nowDate.month == selectedDate.month &&
                nowDate.dayOfMonth.toString() == dayInHolder) {
            holder.dayOfMonth.setTypeface(null, Typeface.BOLD)
            holder.dayOfMonth.textSize = 23f
        }

        holder.dayOfMonth.text = dayInHolder

        if (dayInHolder != "") {
            var dateParams = DateParams(
                _id = null,
                date = LocalDate.now(),
                status = null
            )

            dateParams = calendarViewModel.getDayStatus(dateParams)

            when (dateParams.status) {
                "Есть записи" -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_medium)
                "Занят" -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_busy)
                "Выходной" -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_day_off)
            }
        }
    }

    override fun getItemCount(): Int {
        // возвращает количество объектов в списке
        return daysInMonth.size
    }

    interface OnItemListener {
        // Подключаем интерфейс onItemListener
        fun onItemClick(position: Int, dayText: String?)
    }
}