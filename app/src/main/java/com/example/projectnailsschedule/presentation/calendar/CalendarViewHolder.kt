package com.example.projectnailsschedule.presentation.calendar

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

    init {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        // TODO: делить всю ширину экрана на 7, чтобы рассчитать ширину дней в календаре 
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        // Вызывает onItemClick класса MainActivity
        // Анимация увеличения
        /*val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 15f),
            PropertyValuesHolder.ofFloat("scaleY", 15f)
        )
        scaleUp.duration = 1500
        scaleUp.start()*/
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }
}
