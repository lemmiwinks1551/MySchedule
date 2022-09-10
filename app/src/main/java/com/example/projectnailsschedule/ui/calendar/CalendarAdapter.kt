package com.example.projectnailsschedule.ui.calendar

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.dateStatusDB.DateStatusDbHelper
import java.time.LocalDate

internal class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: CalendarFragment,
    private val dayStatuses: MutableMap<String, String>
) :
    RecyclerView.Adapter<CalendarViewHolder>() {

    private var LOG = "CalendarAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)

        // Выравнивает элементы по высоте
        val layoutParams = view.layoutParams

        // В зависимости от ориентации экрана установить разную высоту для элементов
        // 1 - портретная, 2-горизонтальная
        val orientation = parent.resources.configuration.orientation
        if (orientation == 1) {
            layoutParams.height = (parent.height * 0.10).toInt()
        } else {
            layoutParams.height = (parent.height * 0.15).toInt()
        }

        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        // выполняет привязку объекта ViewHolder к объекту по определенной позиции.
        // Если день имесяц для отправки в холдер текущие - покрасить ячейку
        val nowDate = LocalDate.now()
        val dayInHolder = daysOfMonth[position]

        //Устанавливаем фон для сегодняшнего дня
        if (dayInHolder == nowDate.dayOfMonth.toString() && month == 0) {
            holder.dayOfMonth.setTypeface(null, Typeface.BOLD)
            holder.dayOfMonth.textSize = 23f
        }
        holder.dayOfMonth.text = dayInHolder

        if (dayInHolder != "") {
            when (dayStatuses.getValue(dayInHolder).replace("0", "")) {
                "medium" -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_medium)
                "busy" -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_busy)
                "dayOff" -> holder.dayOfMonth.setBackgroundResource(R.drawable.border_day_off)
            }
        }
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