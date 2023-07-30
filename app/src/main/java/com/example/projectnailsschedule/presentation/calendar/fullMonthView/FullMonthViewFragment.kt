package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.databinding.FragmentFullMonthViewBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.DateWeekAppModel
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV.FullMonthViewRVAdapter
import com.example.projectnailsschedule.util.Util
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class FullMonthViewFragment : Fragment() {
    val log = this::class.simpleName

    private var fullMonthViewVM: FullMonthViewViewModel? = null

    private var _binding: FragmentFullMonthViewBinding? = null
    private var fullMonthAppointmentsRV: RecyclerView? = null
    private var appointmentsRVAdapter: FullMonthViewRVAdapter? = null

    private var prevMonthButton: ImageButton? = null
    private var nextMonthButton: ImageButton? = null
    private var monthTextView: TextView? = null
    private var yearTextView: TextView? = null

    private var appointmentList: MutableList<AppointmentModelDb>? = null
    private var datesList: MutableList<DateParams>? = null

    private val dateRecyclerViewSpanCount = 1
    private val bindingKeyAppointment = "appointmentParams"
    private var dateMonthQuery: String? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        fullMonthViewVM = ViewModelProvider(
            this,
            FullMonthViewModelFactory(context)
        )[FullMonthViewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullMonthViewBinding.inflate(inflater, container, false)

        initWidgets()

        setObservers()

        // swipeToDelete()

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
                weekDay = Util().getDayOfWeek(date),
                appointmentsList = fullMonthViewVM!!.getDateAppointments(dateParams)
            )

            list.add(addToList)
        }

        // create adapter
        val fullMonthViewRVAdapter =
            FullMonthViewRVAdapter(list, requireContext(), findNavController(), fullMonthViewVM!!)

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, 1)

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
        val month =
            SimpleDateFormat("LLLL", Locale("ru")).format(date).replaceFirstChar { it.uppercase() }

        monthTextView!!.text = month
        yearTextView!!.text = fullMonthViewVM!!.selectedMonth.value!!.year.toString()
    }
}