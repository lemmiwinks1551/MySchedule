package com.example.projectnailsschedule.presentation.calendar

import android.graphics.Color
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
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarAdapter
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarViewHolder
import com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView.DateShortAdapter
import com.example.projectnailsschedule.util.Util
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
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
    private var addButton: FloatingActionButton? = null
    private var layout: LinearLayout? = null
    private var dateParams: DateParams? = null

    // control background click listener
    private var prevHolderPos: Int? = null
    private var click: Boolean = false

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
        layout = binding.fragmentCalendar
    }

    private fun initClickListeners() {
        // set click listener on button go_into_date
        binding.goIntoDate.setOnClickListener {
            // start fragment with chosen date
            val bundle = Bundle()
            bundle.putParcelable("dateParams", dateParams)
            binding.goIntoDate.findNavController().navigate(
                R.id.action_nav_calendar_to_dateFragment,
                bundle
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
        // set observer for DateParams
        calendarViewModel?.selectedDateParams?.observe(viewLifecycleOwner) {
            Log.e(log, it.date.toString())
            Log.e(log, dateParams?.date.toString())

            // if year was changed
            if (it.date!!.year != dateParams?.date?.year) {
                setYearTextView(it!!.date!!.year)
            }

            // if month was changed
            if (it.date!!.monthValue != dateParams?.date?.monthValue) {
                setMonthTextView(it)
                inflateCalendarRecyclerView(it.date!!)
            }

            // if day was changed and has appointments
            if (it.appointmentCount != null
            ) {
                inflateShortDateRecyclerView(it)
            }

            // reset local DateParams
            dateParams = DateParams(
                _id = null,
                date = it.date,
                appointmentCount = null
            )
        }
    }

    private fun setMonthTextView(selectedDateParams: DateParams) {
        // set month into textview
        // TODO: поправить формат
        monthTextView?.text =
            selectedDateParams.date!!.month?.getDisplayName(TextStyle.FULL, Locale("eng"))
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
            calendarViewModel = calendarViewModel!!
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 7)

        calendarRecyclerView?.layoutManager = layoutManager
        calendarRecyclerView?.adapter = calendarAdapter
        calendarRecyclerView?.scheduleLayoutAnimation()
    }

    private fun changeMonth(operator: Char) {
        // hide go_into_date button
        addButton?.visibility = View.INVISIBLE

        // make calculations in ViewModel
        calendarViewModel?.changeMonth(operator = operator)

        // clear DateShort RecyclerView
        shortDataRecyclerView?.adapter = null
    }

    override fun onItemClick(position: Int, dayText: String?) {
        // click on day in calendar
        if (!dayText.isNullOrEmpty()) {
            // set button go_into_date and recycler view components visible
            addButton?.visibility = View.VISIBLE
            shortDataRecyclerView?.visibility = View.VISIBLE

            // change selected date in ViewModel
            calendarViewModel?.changeDay(day = dayText.toInt())
        }

        // set current holder and prev view holder
        val holderNew: CalendarViewHolder =
            calendarRecyclerView?.findViewHolderForAdapterPosition(position) as CalendarViewHolder
        var holderOld: CalendarViewHolder? = null

        if (prevHolderPos != null) {
            holderOld  =
                calendarRecyclerView?.findViewHolderForAdapterPosition(prevHolderPos!!) as CalendarViewHolder
        }

        if (position != prevHolderPos) {
            if (!click || position != prevHolderPos) {
                holderNew.cellLayout.setBackgroundColor(Color.RED)
                holderOld?.cellLayout?.setBackgroundResource(R.drawable.calendar_recycler_view_borders)
            } else {
                holderOld?.cellLayout?.setBackgroundResource(R.drawable.calendar_recycler_view_borders)
            }
        } else {
            if (!click) {
                holderNew.cellLayout.setBackgroundColor(Color.RED)
            } else {
                holderOld?.cellLayout?.setBackgroundResource(R.drawable.calendar_recycler_view_borders)
            }
        }


        // set prev and old position
        prevHolderPos = position
        clickSwith()
    }

    fun clickSwith() {
        click = !click
    }

    private fun inflateShortDateRecyclerView(selectedDateParams: DateParams) {
        // create adapter for ShortDateRecyclerVIew
        val dateShortAdapter =
            DateShortAdapter(
                selectedDateParams.appointmentCount!!,
                selectedDateParams,
                calendarViewModel!!
            )

        // create layoutManager with 1 elements in a row
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        // set adapter and layout manager to recycler view
        shortDataRecyclerView?.layoutManager = layoutManager
        shortDataRecyclerView?.adapter = dateShortAdapter
    }

    override fun onResume() {
        Log.e(log, "onResume")
        super.onResume()

        // hide keyboard
        Util().hideKeyboard(requireActivity())

        // Clear views
        shortDataRecyclerView?.adapter = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // set search button visible
        menu.findItem(R.id.search).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroyView() {
        // reset local DateParams
        dateParams = DateParams(
            _id = null,
            date = null,
            appointmentCount = null
        )
        super.onDestroyView()
    }
}