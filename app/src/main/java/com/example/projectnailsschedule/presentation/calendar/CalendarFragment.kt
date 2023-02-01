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
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.dataShort.DateShortAdapter
import com.example.projectnailsschedule.presentation.calendar.dataShort.DateShortGetDbData
import com.example.projectnailsschedule.util.Service
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {

    private val log = this::class.simpleName
    private var calendarViewModel: CalendarViewModel? = null
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var shortDataRecyclerView: RecyclerView? = null
    private var dateTextView: TextView? = null
    private var addButton: FloatingActionButton? = null

    private var layout: LinearLayout? = null

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

        // set binding
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // init all widgets
        initWidgets()

        // set click listener on button go_into_date
        binding.goIntoDate.setOnClickListener {
            // start fragment with chosen date
            it.findNavController().navigate(
                R.id.action_nav_calendar_to_dateFragment,
                calendarViewModel?.selectDate()
            )
        }

        // set click listener on button next_month
        binding.nextMonth.setOnClickListener {
            changeMonth(operator = '+')
        }

        // set click listener on button previous_month
        binding.prevMonth.setOnClickListener {
            changeMonth(operator = '-')
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initWidgets() {
        // Инициировать view
        calendarRecyclerView = binding.calendarRecyclerView
        shortDataRecyclerView = binding.shortDataRecyclerView
        monthYearText = binding.monthYearText
        addButton = binding.goIntoDate
        dateTextView = binding.dayTextView
        layout = binding.fragmentCalendar
    }

    private fun setMonthView() {
        // set month and year name into textview
        monthYearText?.text = calendarViewModel?.getMonthYearName()

        // get
        val daysInMonth = calendarViewModel?.currentMonth?.let { daysInMonthArray(it) } // ??

        // Создаем CalendarAdapter, передаем количество дней в месяце и listener
        val calendarAdapter =
            daysInMonth?.let {
                CalendarAdapter(
                    it,
                    this,
                    calendarViewModel!!,
                    String.format("${calendarViewModel?.day}.${calendarViewModel?.day}.${calendarViewModel?.day}")
                )
            }

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
        val firstOfMonth: LocalDate = calendarViewModel?.currentMonth?.withDayOfMonth(1) ?: LocalDate.now()

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

    private fun changeMonth(operator: Char) {
        // hide go_into_date button
        addButton?.visibility = View.INVISIBLE

        // make calculations
        calendarViewModel?.changeMonth(operator = operator)
        setMonthView()

        // clear DateShort RecyclerView
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

            val date = Date.from(calendarViewModel?.currentMonth?.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
            calendarViewModel?.day = Service().addZero(dayText)
            calendarViewModel?.month = SimpleDateFormat("MM", Locale.getDefault()).format(date)
            calendarViewModel?.year = calendarViewModel?.currentMonth?.year.toString()

            dateTextView?.text = String.format("${calendarViewModel?.day}.${calendarViewModel?.month}.${calendarViewModel?.year}")

            shortDate(calendarViewModel?.day.toString(), calendarViewModel?.day.toString(), calendarViewModel?.year.toString()) // Отрисовать предпросмотр выбранного дня
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
        shortDate(calendarViewModel?.day.toString(), calendarViewModel?.month.toString(), calendarViewModel?.year.toString())

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
        return "${calendarViewModel?.day}.${calendarViewModel?.month}.${calendarViewModel?.year}"
    }
}