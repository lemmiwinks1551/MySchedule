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
import com.example.projectnailsschedule.presentation.calendar.dataShort.DateShortAdapter
import com.example.projectnailsschedule.presentation.calendar.dataShort.DateShortGetDbData
import com.example.projectnailsschedule.util.Util
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {

    private val log = this::class.simpleName
    private var calendarViewModel: CalendarViewModel? = null
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var monthTextView: TextView? = null
    private var yearTextView: TextView? = null
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

        // init click listeners
        initClickListeners()

        // inti observers
        initObservers()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initWidgets() {
        // init all widgets
        calendarRecyclerView = binding.calendarRecyclerView
        shortDataRecyclerView = binding.shortDataRecyclerView
        monthTextView = binding.monthTextView
        yearTextView = binding.yearTextView
        addButton = binding.goIntoDate
        dateTextView = binding.dayTextView
        layout = binding.fragmentCalendar
    }

    private fun initClickListeners() {
        // set click listener on button go_into_date
        binding.goIntoDate.setOnClickListener {
            // start fragment with chosen date
            it.findNavController().navigate(
                R.id.action_nav_calendar_to_dateFragment,
                calendarViewModel?.goIntoDate()
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
    }

    private fun initObservers() {
        // set observer for TextViews and ShortDateRecyclerView
        calendarViewModel?.selectedDate?.observe(viewLifecycleOwner) {
            if (it != null) {
                setMonthTextView(it)
                inflateShortDateRecyclerView(it)
                setShortDateTextView(it)
            }
        }

        // set month observer for CalendarRecyclerView
        calendarViewModel?.selectedMonth?.observe(viewLifecycleOwner) {
            if (it != null) {
                inflateCalendarRecyclerView(it)
            }
        }

        // set year observer for YearTextView
        calendarViewModel?.selectedYearValue?.observe(viewLifecycleOwner) {
            if (it != null) {
                setYearTextView(it)
            }
        }
    }

    private fun setMonthTextView(selectedDate: LocalDate) {
        // set month into textview
        // TODO: поправить формат
        monthTextView?.text = selectedDate.month.getDisplayName(TextStyle.FULL, Locale("eng"))
    }

    private fun setYearTextView(yearValue: Int) {
        // set year into textview
        yearTextView?.text = yearValue.toString()
    }

    private fun inflateCalendarRecyclerView(selectedDate: LocalDate) {
        // get array of days from selected month
        val daysInMonth: ArrayList<String> = Util().getArrayFromMonth(selectedDate)

        // create adapter
        val calendarAdapter = CalendarAdapter(
            daysInMonth = daysInMonth,
            onItemListener = this,
            calendarViewModel = calendarViewModel!!,
            selectedDate = selectedDate
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 7)

        calendarRecyclerView?.layoutManager = layoutManager
        calendarRecyclerView?.adapter = calendarAdapter
    }

    private fun changeMonth(operator: Char) {
        // hide go_into_date button
        addButton?.visibility = View.INVISIBLE

        // make calculations in ViewModel
        calendarViewModel?.changeMonth(operator = operator)

        // clear DateShort RecyclerView
        shortDataRecyclerView?.adapter = null
        dateTextView?.text = null
        calendarRecyclerView?.scheduleLayoutAnimation()

    }

    override fun onItemClick(position: Int, dayText: String?) {
        // click on day in calendar
        if (!dayText.isNullOrEmpty()) {
            // set button go_into_date and recycler view components visible
            addButton?.visibility = View.VISIBLE
            shortDataRecyclerView?.visibility = View.VISIBLE
            dateTextView?.visibility = View.VISIBLE

            // change selected date in ViewModel
            calendarViewModel?.changeDay(day = dayText.toInt())
        }
    }

    private fun inflateShortDateRecyclerView(selectedDate: LocalDate) {
        // TODO: метод будет получать данные из класса DateShortGetDb и устанавливать в RecyclerView
        val dateShortDbData = DateShortGetDbData(selectedDate, this.requireContext())

        dateShortDbData.fetchDate()

        // Создаем CalendarAdapter, передаем количество строк в курсоре
        val dateShortAdapter =
            DateShortAdapter(
                dateShortDbData.getDataRows(),
                dateShortDbData,
                String.format("${selectedDate.dayOfMonth}.${selectedDate.monthValue}.${selectedDate.year}")
            )

        // Создаем layoutManager и устанавливает способ отображения элементов в нем
        // GridLayoutManager упорядочивает элементы в виде таблицы со столлбцами и строками (1 элемент в ряд)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        // Устанавливаем в RecyclerView менеджера и адаптер
        shortDataRecyclerView?.layoutManager = layoutManager
        shortDataRecyclerView?.adapter = dateShortAdapter
    }

    private fun setShortDateTextView(selectedDate: LocalDate) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        dateTextView?.text = selectedDate.format(formatter)
    }

    override fun onResume() {
        Log.e(log, "onResume")
        super.onResume()

        // Убираем клавиатуру
        Util().hideKeyboard(requireActivity())

        // Clear views
        dateTextView?.text = null
        shortDataRecyclerView?.adapter = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Устанавливаем иконку поиска видимой (только для фрагмента CalendarFragment)
        menu.findItem(R.id.search).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroyView() {
        Log.e(log, "onDestroyView")
        super.onDestroyView()
        _binding = null
    }
}