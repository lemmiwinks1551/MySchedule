package com.example.projectnailsschedule.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null

    private fun initWidgets() {
        // Инициировать view
        calendarRecyclerView = binding.calendarRecyclerView
        monthYearText = binding.monthYearTV
    }

    private fun setMonthView() {
        // Устанавливаем название месяца в TextView
        monthYearText?.text = monthYearFromDate()
        val daysInMonth = selectedDate?.let { daysInMonthArray(it) }

        // Создаем CalendarAdapter, передаем количество дней в месяце и listener
        val calendarAdapter = daysInMonth?.let { CalendarAdapter(it, this) }

        // Создаем layoutManager и устанавливает способ отображения элементов в нем
        // GridLayoutManager упорядочивает элементы в виде грида со столлбцами и строками (7 элементов в ряд)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 7)

        // Устанавливаем в RecyclerView менеджера и адаптер
        calendarRecyclerView?.layoutManager = layoutManager
        calendarRecyclerView?.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        // Считаеи и возвращаем массив дней в месяце
        val daysInMonthArray = ArrayList<String>()

        // Получаем месяц
        val yearMonth = YearMonth.from(date)

        // Получаем длину месяца
        val daysInMonth = yearMonth.lengthOfMonth()

        // Получаем первый день текущего месяца
        val firstOfMonth: LocalDate = selectedDate!!.withDayOfMonth(1)

        // Получаем день недели первого дня месяца
        val dayOfWeek = firstOfMonth.dayOfWeek.value - 1

        // Заполняем массив для отображения в RecyclerView
        // Учитываем пустые дни (дни прошлого месяца
        // TODO: 12.07.2022 Добавить дни прошлого и будущего месяцев
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(): String? {
        // Метод форматирует название месяца и год для отображения во View
        val date = Date.from(selectedDate?.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
        val month = SimpleDateFormat("LLLL", Locale.getDefault()).format(date)
        val year: String = selectedDate?.year.toString()
        return "$month $year"
    }

    private fun previousMonthAction() {
        // Обработка нажатия на кнопку Предыдущий месяц
        // Вычитаем один месяц из текущего
        selectedDate = selectedDate?.minusMonths(1)
        CalendarAdapter.month--
        setMonthView()
    }

    private fun nextMonthAction() {
        // Обработка нажатия на кнопку Следующий месяц
        // Добавляем один месяц к текущему
        selectedDate = selectedDate?.plusMonths(1)
        CalendarAdapter.month++
        setMonthView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.e("LifeCycle", "CalendarFragment created")
        // Создаем переменную ViewModel
        val calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Вызываем метод, который инициализирует View
        initWidgets()

        // Получить сегодняшню дату yyyy-MM-dd
        selectedDate = LocalDate.now()

        // Вызываем метод, который устанавливает название месяца, создает и устанавливает адаптер и менеджер
        setMonthView()

        binding.nextMonth.setOnClickListener {
            previousMonthAction()
        }

        binding.prevMonth.setOnClickListener {
            nextMonthAction()
        }

        return root
    }

    override fun onDestroyView() {
        Log.e("LifeCycle", "CalendarFragment onDestroy")
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "") {
            // TODO: 12.07.2022 Реализовать логику перехода на экран выбранной даты
            val message = "Selected Date $dayText"
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }
}



