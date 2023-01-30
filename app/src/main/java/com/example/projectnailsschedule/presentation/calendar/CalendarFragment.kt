package com.example.projectnailsschedule.presentation.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentCalendarBinding
import com.example.projectnailsschedule.presentation.appointment.AppointmentViewModel
import com.example.projectnailsschedule.presentation.appointment.AppointmentViewModelFactory
import com.example.projectnailsschedule.util.Service
import com.example.projectnailsschedule.presentation.calendar.dataShort.DateShortAdapter
import com.example.projectnailsschedule.presentation.calendar.dataShort.DateShortGetDbData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
    companion object {
        var day = ""
        var month = ""
        var year = ""
        var width = 0 // ??
    }

    private var calendarViewModel: CalendarViewModel? = null
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val statusesMap = StatusesMap()
    private val binding get() = _binding!!

    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var shortDataRecyclerView: RecyclerView? = null
    private var dateTextView: TextView? = null
    private var addButton: FloatingActionButton? = null
    private var selectedDate: LocalDate? = null
    private var additionMonth: Long = 0
    private var layout: LinearLayout? = null
    private val log = this::class.simpleName

    private fun initWidgets() {
        // Инициировать view
        calendarRecyclerView = binding.calendarRecyclerView
        shortDataRecyclerView = binding.shortDataRecyclerView
        monthYearText = binding.monthYearTV
        addButton = binding.addData
        dateTextView = binding.dayTextView
        layout = binding.fragmentCalendar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        calendarViewModel = ViewModelProvider(
            this,
            CalendarViewModelFactory(context)
        )[CalendarViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        if (savedInstanceState != null) {
            additionMonth = savedInstanceState.getLong("additionMonth")
        }

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.addData.setOnClickListener {
            /** Start fragment */

            /** Bundle содержит дату, которую выбрал пользователь */
            val bundle = Bundle()
            bundle.putString(
                "date",
                "$day.$month.$year"
            )

            /** Открываем DateFragment с переданной датой */
            // TODO: Add SelectDate method
            it.findNavController().navigate(R.id.action_nav_calendar_to_dateFragment, bundle)
        }

        // Вызываем метод, который инициализирует View
        initWidgets()

        // Получить сегодняшню дату yyyy-MM-dd
        selectedDate = LocalDate.now().plusMonths(additionMonth)

        binding.nextMonth.setOnClickListener {
            selectNextMonth()
        }

        binding.prevMonth.setOnClickListener {
            selectPreviousMonth()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.e(log, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putLong("additionMonth", additionMonth)
    }

    private fun setMonthView() {
        // Устанавливаем название месяца в TextView
        monthYearText?.text = monthYearFromDate()
        val daysInMonth = selectedDate?.let { daysInMonthArray(it) }

        // Создаем CalendarAdapter, передаем количество дней в месяце и listener
        val calendarAdapter =
            daysInMonth?.let { CalendarAdapter(it,
                this,
                calendarViewModel!!,
            String.format("$day.$month.$year")) }

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

        return daysInMonthArray
    }

    private fun monthYearFromDate(): String {
        // Метод форматирует название месяца и год для отображения во View
        val date = Date.from(selectedDate?.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
        val month = SimpleDateFormat("LLLL", Locale.getDefault()).format(date)
        val year: String = selectedDate?.year.toString()
        return "$month $year"
    }

    private fun selectPreviousMonth() {
        // Обработка нажатия на кнопку Предыдущий месяц
        // Вычитаем один месяц из текущего
        addButton?.visibility = View.INVISIBLE

        selectedDate = selectedDate?.minusMonths(1)
        CalendarAdapter.month--
        additionMonth--
        setMonthView()
        shortDataRecyclerView?.adapter = null
        dateTextView?.text = null
        calendarRecyclerView?.scheduleLayoutAnimation()
    }

    private fun selectNextMonth() {
        // Обработка нажатия на кнопку Следующий месяц
        // Добавляем один месяц к текущему
        addButton?.visibility = View.INVISIBLE

        selectedDate = selectedDate?.plusMonths(1)
        CalendarAdapter.month++
        additionMonth++
        setMonthView()
        shortDataRecyclerView?.adapter = null
        dateTextView?.text = null
        calendarRecyclerView?.scheduleLayoutAnimation()
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (!dayText.isNullOrEmpty()) {
            // Отобразить педварительный просмотр и кнопку
            addButton?.visibility = View.VISIBLE
            shortDataRecyclerView?.visibility = View.VISIBLE
            dateTextView?.visibility = View.VISIBLE

            val date = Date.from(selectedDate?.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
            day = Service().addZero(dayText)
            month = SimpleDateFormat("MM", Locale.getDefault()).format(date)
            year = selectedDate?.year.toString()

            dateTextView?.text = String.format("${day}.${month}.${year}")

            shortDate(day, month, year) // Отрисовать предпросмотр выбранного дня
        } else {
            // Убрать предварительный просмотр и кнопку
            addButton?.visibility = View.INVISIBLE
            shortDataRecyclerView?.visibility = View.INVISIBLE
            dateTextView?.text = "Выберите дату из календаря"
        }
    }

    private fun shortDate(date: String, month: String, year: String) {
        // TODO: метод будет получать данные из класса DateShortGetDb и устанавливать в RecyclerView
        val dateShortDbData = DateShortGetDbData(date, month, year, this.requireContext())

        dateShortDbData.fetchDate()

        // Создаем CalendarAdapter, передаем количество строк в курсоре
        val calendarAdapter =
            DateShortAdapter(
                dateShortDbData.getDataRows(),
                dateShortDbData,
                String.format("${date}.${month}.${year}")
            )

        // Создаем layoutManager и устанавливает способ отображения элементов в нем
        // GridLayoutManager упорядочивает элементы в виде таблицы со столлбцами и строками (1 элемент в ряд)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        // Устанавливаем в RecyclerView менеджера и адаптер
        shortDataRecyclerView?.layoutManager = layoutManager
        shortDataRecyclerView?.adapter = calendarAdapter
    }

    override fun onResume() {
        Log.e(log, "onResume")
        super.onResume()

        // Вызываем метод, который устанавливает название месяца, создает и устанавливает адаптер и менеджер
        setMonthView()

        // Обновляет выбранную дату в предварительном просмотре
        shortDate(day, month, year)

        // Убираем клавиатуру
        Service().hideKeyboard(requireActivity())

        // Clear views
        dateTextView?.text = null
        shortDataRecyclerView?.adapter = null
    }

    override fun onDestroy() {
        Log.e(log, "onDestroy")
        CalendarAdapter.month = 0
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.e(log, "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Устанавливаем иконку поиска видимой (только для фрагмента CalendarFragment)
        menu.findItem(R.id.search).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    fun getSelectedDate(): String {
        // Return last selected date
        return "$day.$month.$year"
    }
}