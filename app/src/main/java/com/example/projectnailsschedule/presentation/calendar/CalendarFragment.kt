package com.example.projectnailsschedule.presentation.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentCalendarBinding
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarRvAdapter
import com.example.projectnailsschedule.presentation.calendar.dateShortRecyclerView.DateShortAdapter
import com.example.projectnailsschedule.util.Util
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class CalendarFragment : Fragment(),
    DateShortAdapter.OnItemListener {

    private val log = this::class.simpleName
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var monthTextView: TextView? = null
    private var yearTextView: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var shortDataRecyclerView: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var layout: ConstraintLayout? = null
    private var currentDate: DateParams? = null

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
            bundle.putParcelable("dateParams", currentDate)
            binding.goIntoDate.findNavController().navigate(
                R.id.action_nav_calendar_to_dateFragment,
                bundle
            )
        }

        // set click listener on button next_month
        binding.nextMonth.setOnClickListener {
            changeMonth(operator = true)
        }

        // set click listener on button previous_month
        binding.prevMonth.setOnClickListener {
            changeMonth(operator = false)
        }
    }

    private fun initObservers() {
        // set observer for DateParams
        calendarViewModel.selectedDate.observe(viewLifecycleOwner) {
            Log.i(log, "Current date: ${currentDate?.date}")
            Log.i(log, "Selected date: ${it.date}")

            // if year was changed
            if (it.date!!.year != currentDate?.date?.year) {
                // update yearTextView
                setYearTextView(it!!.date!!.year)
            }

            // if month was changed
            if (it.date!!.monthValue != currentDate?.date?.monthValue) {
                // update monthTextView
                setMonthTextView(it)
                inflateCalendarRecyclerView(it.date!!)
            }

            // if day was changed and has appointments
            if (it.appointmentCount != null
            ) {
                inflateShortDateRecyclerView(it)
            }

            // update local DateParams
            currentDate = DateParams(
                _id = null,
                date = it.date,
                appointmentCount = null
            )
        }

        calendarViewModel.visibility.observe(viewLifecycleOwner) {
            if (it) {
                addButton?.visibility = View.VISIBLE
            } else {
                addButton?.visibility = View.INVISIBLE
            }
        }
    }

    private fun setMonthTextView(selectedDateParams: DateParams) {
        // update monthTextView
        val date =
            Date.from(selectedDateParams.date?.atStartOfDay(ZoneId.systemDefault())?.toInstant())

        val calendar = Calendar.getInstance()
        calendar.time = date
        val monthResource = when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> R.string.january
            Calendar.FEBRUARY -> R.string.february
            Calendar.MARCH -> R.string.march
            Calendar.APRIL -> R.string.april
            Calendar.MAY -> R.string.may
            Calendar.JUNE -> R.string.june
            Calendar.JULY -> R.string.july
            Calendar.AUGUST -> R.string.august
            Calendar.SEPTEMBER -> R.string.september
            Calendar.OCTOBER -> R.string.october
            Calendar.NOVEMBER -> R.string.november
            Calendar.DECEMBER -> R.string.december
            else -> 0
        }
        val formattedMonth = getString(monthResource)

        monthTextView?.text = formattedMonth
    }

    private fun setYearTextView(yearValue: Int) {
        // update yearTextView
        yearTextView?.text = yearValue.toString()
    }

    private fun inflateCalendarRecyclerView(selectedDate: LocalDate) {
        // get array of days from selected month
        val daysInMonth: ArrayList<String> = Util().getArrayFromMonth(selectedDate)
        // create adapter
        val calendarRvAdapter = CalendarRvAdapter(
            daysInMonth = daysInMonth,
            calendarViewModel = calendarViewModel,
            context = requireContext()
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 7)

        calendarRecyclerView?.layoutManager = layoutManager
        calendarRecyclerView?.adapter = calendarRvAdapter
        calendarRecyclerView?.scheduleLayoutAnimation()
    }

    private fun inflateShortDateRecyclerView(selectedDateParams: DateParams) {
        // Make View visible
        if (shortDataRecyclerView!!.visibility == View.INVISIBLE) {
            shortDataRecyclerView!!.visibility = View.VISIBLE
        }

        // create adapter for ShortDateRecyclerVIew
        val dateShortAdapter =
            DateShortAdapter(
                calendarViewModel.getArrayAppointments(selectedDateParams).size,
                selectedDateParams,
                calendarViewModel,
                this
            )

        // create layoutManager with 1 elements in a row
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

        // set adapter and layout manager to recycler view
        shortDataRecyclerView?.layoutManager = layoutManager
        shortDataRecyclerView?.adapter = dateShortAdapter
    }

    private fun changeMonth(operator: Boolean) {
        // hide go_into_date button
        addButton?.visibility = View.INVISIBLE

        // make calculations in ViewModel
        calendarViewModel.changeMonth(operator = operator)

        // clear DateShort RecyclerView
        shortDataRecyclerView?.adapter = null
    }

    override fun onResume() {
        Log.e(log, "onResume")
        RuStoreAd().banner(requireContext(), binding.root)

        super.onResume()

        // hide keyboard
        Util().hideKeyboard(requireActivity())

        addButton?.visibility = View.INVISIBLE
        shortDataRecyclerView!!.visibility = View.INVISIBLE
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // set search button visible
        menu.findItem(R.id.search).isVisible = true
        menu.findItem(R.id.full_month_view).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroyView() {
        // reset local DateParams
        currentDate = DateParams(
            _id = null,
            date = null,
            appointmentCount = null
        )
        _binding = null
        super.onDestroyView()
    }

    override fun onItemClickShortDate() {
        // start fragment with chosen date
        val bundle = Bundle()
        bundle.putParcelable("dateParams", currentDate)
        binding.goIntoDate.findNavController().navigate(
            R.id.action_nav_calendar_to_dateFragment,
            bundle
        )
    }
}