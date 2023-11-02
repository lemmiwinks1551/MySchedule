package com.example.projectnailsschedule.presentation.calendar.listMonthView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.projectnailsschedule.databinding.FragmentFullMonthViewBinding
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.DateWeekAppModel
import com.example.projectnailsschedule.presentation.calendar.listMonthView.fullMonthViewRV.FullMonthViewRVAdapter
import com.example.projectnailsschedule.util.Util
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class ListMonthViewFragment : Fragment() {
    val log = this::class.simpleName

    private val fullMonthViewVM: ListMonthViewModel by viewModels()

    private var _binding: FragmentFullMonthViewBinding? = null
    private var fullMonthAppointmentsRV: RecyclerView? = null

    private var prevMonthButton: ImageButton? = null
    private var nextMonthButton: ImageButton? = null
    private var monthTextView: TextView? = null
    private var yearTextView: TextView? = null
    lateinit var layoutManager: LayoutManager

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullMonthViewBinding.inflate(inflater, container, false)

        initWidgets()

        setObservers()

        initClickListeners()

        return binding.root
    }

    private fun initWidgets() {
        fullMonthAppointmentsRV = binding.fullMonthAppointmentsRV
        nextMonthButton = binding.nextMonth
        prevMonthButton = binding.prevMonth
        monthTextView = binding.monthTextView
        yearTextView = binding.yearTextView
    }

    private fun setObservers() {
        fullMonthViewVM!!.selectedMonth.observe(viewLifecycleOwner) { selectedMonth ->
            inflateCalendarRecyclerView(selectedMonth)
            setMonthAndYear() // update year and month in text view
        }
    }

    private fun inflateCalendarRecyclerView(selectedDate: LocalDate) {
        // get array of days from selected month

        val daysInMonth: ArrayList<String> = Util().getArrayFromMonth2(selectedDate)

        val list = mutableListOf<DateWeekAppModel>()

        for (i in 1..selectedDate.lengthOfMonth()) {

            val dateFormat =
                Util().dateConverterNew(fullMonthViewVM!!.selectedMonth.value.toString())
            var date = dateFormat.drop(2)
            date = daysInMonth[i - 1] + date
            date = Util().dateConverter(date)

            val localDate = Util().convertStringToLocalDate(date)

            val dateParams = DateParams(_id = null, date = localDate, appointmentCount = null)

            val addToList = DateWeekAppModel(
                date = dateParams.date!!,
                weekDay = Util().getDayOfWeek(date, requireContext()),
                appointmentsList = fullMonthViewVM!!.getDateAppointments(dateParams)
            )

            list.add(addToList)
        }

        // create adapter
        val fullMonthViewRVAdapter =
            FullMonthViewRVAdapter(list, requireContext(), findNavController(), fullMonthViewVM!!)

        layoutManager = GridLayoutManager(activity, 1)

        fullMonthAppointmentsRV?.layoutManager = layoutManager
        fullMonthAppointmentsRV?.adapter = fullMonthViewRVAdapter
    }

    private fun initClickListeners() {
        nextMonthButton!!.setOnClickListener {
            fullMonthViewVM!!.changeMonth(true)
        }
        prevMonthButton!!.setOnClickListener {
            fullMonthViewVM!!.changeMonth(false)
        }
    }

    private fun setMonthAndYear() {
        // set month name
        val date = Date.from(
            fullMonthViewVM!!.selectedMonth.value!!.atStartOfDay(ZoneId.systemDefault())
                ?.toInstant()
        )
        val month = SimpleDateFormat("LLLL", Locale.getDefault()).format(date).replaceFirstChar { it.uppercase() }

        monthTextView!!.text = month
        yearTextView!!.text = fullMonthViewVM!!.selectedMonth.value!!.year.toString()
    }

    override fun onResume() {
        super.onResume()
        // scroll to previous position
        fullMonthAppointmentsRV?.post {
            val oldPosition = fullMonthViewVM!!.oldPosition
            val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = oldPosition
            layoutManager.startSmoothScroll(smoothScroller)
        }
        RuStoreAd().banner(requireContext(), binding.root)
    }
}