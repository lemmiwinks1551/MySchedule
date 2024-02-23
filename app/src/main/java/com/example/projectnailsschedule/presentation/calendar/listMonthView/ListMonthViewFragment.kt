package com.example.projectnailsschedule.presentation.calendar.listMonthView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ListMonthViewFragment : Fragment() {

    private val fullMonthViewVM: ListMonthViewModel by viewModels()

    private var _binding: FragmentFullMonthViewBinding? = null
    private var fullMonthAppointmentsRV: RecyclerView? = null

    private var layoutManager: LayoutManager? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullMonthViewBinding.inflate(inflater, container, false)

        initViews()

        setObservers()

        initClickListeners()

        return binding.root
    }

    private fun initViews() {
        fullMonthAppointmentsRV = binding.fullMonthAppointmentsRV

    }

    private fun setObservers() {
        fullMonthViewVM.selectedMonth.observe(viewLifecycleOwner) { selectedMonth ->
            inflateCalendarRecyclerView(selectedMonth)
            setMonthAndYear() // update year and month in text view
        }
    }

    private fun inflateCalendarRecyclerView(selectedDate: LocalDate) {
        // get array of days from selected month
        val daysInMonth: ArrayList<String> = Util().getArrayFromMonth2(selectedDate)

        val list = mutableListOf<DateWeekAppModel>()
        lifecycleScope.launch {
            for (i in 1..selectedDate.lengthOfMonth()) {
                // формируем первый день месяца и созадем с ним DateParams
                val dateFormat =
                    Util().dateConverterNew(fullMonthViewVM.selectedMonth.value.toString())
                var date = dateFormat.drop(2)
                date = daysInMonth[i - 1] + date
                date = Util().dateConverter(date)
                val localDate = Util().convertStringToLocalDate(date)
                val dateParams = DateParams(_id = null, date = localDate)

                // добавляем в лист объект DateWeekAppModel, который содержит список записей по дню
                val addToList = DateWeekAppModel(
                    date = dateParams.date!!,
                    weekDay = Util().getDayOfWeek(date, requireContext()),
                    appointmentsList = fullMonthViewVM.getDateAppointments(dateParams.date!!)
                )
                list.add(addToList)
            }
            withContext(Dispatchers.Main) {
                // create adapter
                val fullMonthViewRVAdapter =
                    FullMonthViewRVAdapter(list, requireContext(), findNavController(), fullMonthViewVM)

                layoutManager = GridLayoutManager(activity, 1)

                fullMonthAppointmentsRV?.layoutManager = layoutManager
                fullMonthAppointmentsRV?.adapter = fullMonthViewRVAdapter
                fullMonthAppointmentsRV?.scheduleLayoutAnimation()

            }
        }
    }

    private fun initClickListeners() {
        binding.nextMonthButton.setOnClickListener {
            fullMonthViewVM.changeMonth(true)
        }

        binding.prevMonthButton.setOnClickListener {
            fullMonthViewVM.changeMonth(false)
        }
    }

    private fun setMonthAndYear() {
        // set month name
        val date = Date.from(
            fullMonthViewVM.selectedMonth.value!!.atStartOfDay(ZoneId.systemDefault())
                ?.toInstant()
        )
        val month = SimpleDateFormat("LLLL", Locale.getDefault()).format(date).replaceFirstChar { it.uppercase() }

        binding.monthTextView.text = month
        binding.yearTextView.text = fullMonthViewVM.selectedMonth.value!!.year.toString()
    }

    override fun onResume() {
        super.onResume()
        // scroll to previous position
        if (layoutManager != null) {
            fullMonthAppointmentsRV?.post {
                val oldPosition = fullMonthViewVM.oldPosition
                val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                smoothScroller.targetPosition = oldPosition
                layoutManager!!.startSmoothScroll(smoothScroller)
            }
        }
        RuStoreAd().banner(requireContext(), binding.root)
    }
}