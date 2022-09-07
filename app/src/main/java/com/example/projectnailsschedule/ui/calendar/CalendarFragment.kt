package com.example.projectnailsschedule.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.DateActivity
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
    private val statusesMap = StatusesMap()
    private val binding get() = _binding!!
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null
    private var additionMonth: Long = 0
    private val LOG = "CalendarFragment"

    private fun initWidgets() {
        // Инициировать view
        calendarRecyclerView = binding.calendarRecyclerView
        monthYearText = binding.monthYearTV
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.e(LOG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putLong("additionMonth", additionMonth)
    }

    private fun setMonthView() {
        // Устанавливаем название месяца в TextView
        monthYearText?.text = monthYearFromDate()
        val daysInMonth = selectedDate?.let { daysInMonthArray(it) }

        // Создаем CalendarAdapter, передаем количество дней в месяце и listener
        val calendarAdapter =
            daysInMonth?.let { CalendarAdapter(it, this, statusesMap.dayStatuses) }

        // Создаем layoutManager и устанавливает способ отображения элементов в нем
        // GridLayoutManager упорядочивает элементы в виде таблицы со столлбцами и строками (7 элементов в ряд)
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

        // Запускаем поток для выгрузки статусов
        runStatusesMapThread(daysInMonthArray, yearMonth)
        return daysInMonthArray
    }

    private fun monthYearFromDate(): String {
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
        additionMonth--
        setMonthView()
    }

    private fun nextMonthAction() {
        // Обработка нажатия на кнопку Следующий месяц
        // Добавляем один месяц к текущему
        selectedDate = selectedDate?.plusMonths(1)
        CalendarAdapter.month++
        additionMonth++
        setMonthView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.e(LOG, "onCreateView")
        if (savedInstanceState != null) {
            additionMonth = savedInstanceState.getLong("additionMonth")
        }
        // Создаем переменную ViewModel
        val calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Вызываем метод, который инициализирует View
        initWidgets()

        // Получить сегодняшню дату yyyy-MM-dd
        selectedDate = LocalDate.now().plusMonths(additionMonth)

        binding.nextMonth.setOnClickListener {
            nextMonthAction()
        }

        binding.prevMonth.setOnClickListener {
            previousMonthAction()
        }

        return root
    }

    override fun onDestroyView() {
        Log.e(LOG, "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "" && dayText != null) {

            val date = Date.from(selectedDate?.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
            val day = if (dayText.length == 1) String.format("0$dayText") else dayText
            val month = SimpleDateFormat("MM", Locale.getDefault()).format(date)
            val year: String = selectedDate?.year.toString()

            // TODO: Переделать, по клику должен заполняться фрагмент под календарем,
            val intent = Intent(activity, DateActivity::class.java)
            intent.putExtra("day", String.format("$day.$month.$year"))
            activity?.startActivity(intent)
        }
    }

    override fun onResume() {
        Log.e(LOG, "onResume")
        super.onResume()

        // Вызываем метод, который устанавливает название месяца, создает и устанавливает адаптер и менеджер
        setMonthView()
    }

    override fun onDestroy() {
        Log.e(LOG, "onDestroy")
        CalendarAdapter.month = 0

        super.onDestroy()
    }

    override fun onDetach() {
        Log.e(LOG, "onDetach")
        super.onDetach()
    }

    override fun onPause() {
        Log.e(LOG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.e(LOG, "onStop")
        super.onStop()
    }

    private fun runStatusesMapThread(daysInMonthArray: ArrayList<String>, yearMonth: YearMonth) {
        // Запускаем новый поток, который формирует словарь для отрисовки интерфейса
        statusesMap.name = "StatusesMap Thread"
        statusesMap.setDaysOfMonth(daysInMonthArray)
        statusesMap.setYearMonth(yearMonth)
        statusesMap.setContext(this.requireContext())

        // Если поток уже создан - перезапустить
        if (statusesMap.state == Thread.State.NEW) {
            statusesMap.start();
        } else {
            statusesMap.run()
        }
    }

}