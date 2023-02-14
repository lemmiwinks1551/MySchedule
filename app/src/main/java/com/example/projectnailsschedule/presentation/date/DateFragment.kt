package com.example.projectnailsschedule.presentation.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentDateBinding
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarAdapter
import com.example.projectnailsschedule.presentation.date.dateRecyclerView.DateAdapter
import com.example.projectnailsschedule.util.Util
import java.util.ArrayList


class DateFragment : Fragment(), DateAdapter.OnItemListener {
    val log = this::class.simpleName

    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    private val bindingKey = "dateParams"
    private val bindingKeyAppointment = "appointmentParams"
    private val dateRecyclerViewSpanCount = 1

    private var dayStatusSpinner: Spinner? = null
    private var dateParams: DateParams? = null
    private var dateViewModel: DateViewModel? = null
    private var dateRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        dateViewModel = ViewModelProvider(
            this,
            DataViewModelFactory(context)
        )[DateViewModel::class.java]

        // get dateParams from Bundle
        dateParams = arguments?.getParcelable(bindingKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDateBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init clickListeners
        initClickListeners()

        // set current date in viewModel
        dateViewModel!!.selectedDateParams.value = dateParams

        // get status and appointments from date
        dateViewModel?.updateDateParams()

        // set observers
        setObservers()

        // set current status in spinner
        setStatusInSpinner()

        return binding.root
    }

    private fun initWidgets() {
        dateRecyclerView = binding.scheduleRecyclerView
        dayStatusSpinner = binding.spinnerStatus
    }

    private fun initClickListeners() {
        // add new appointment
        binding.addButton.setOnClickListener {
            val appointmentParams = AppointmentParams(
                _id = null,
                appointmentDate = dateParams?.date,
                clientName = null,
                startTime = null,
                procedureName = null,
                phoneNum = null,
                misc = null
            )
            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)
            it.findNavController().navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }

        // click on spinner
        dayStatusSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val spinnerText = dayStatusSpinner?.selectedItem
                    Toast.makeText(
                        context,
                        "Статус дня $spinnerText",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun setObservers() {
        // dateParams observer
        dateViewModel?.selectedDateParams?.observe(viewLifecycleOwner) {
            if (it != null) {
                inflateDateRecyclerView(it)
            }
        }
    }

    private fun inflateDateRecyclerView(selectedDate: DateParams) {
        // create adapter
        val dateAdapter = DateAdapter(
            appointmentsCount = selectedDate.appointmentCount!!,
            onItemListener = this,
            dateViewModel = dateViewModel!!
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, dateRecyclerViewSpanCount)

        dateRecyclerView?.layoutManager = layoutManager
        dateRecyclerView?.adapter = dateAdapter
    }

    override fun onResume() {
        super.onResume()
        // hide keyboard
        Util().hideKeyboard(requireActivity())
    }

    private fun setStatusInSpinner() {
        // set current status in spinner by index fo statusArray
        val statusArray: String = getString(R.string.status_description)
        val statusIndex = statusArray.indexOf(statusArray)
        dayStatusSpinner?.setSelection(statusIndex)
    }

    override fun onItemClick(position: Int) {
        // click on appointment
        Toast.makeText(context, "Appointment has been clicked", Toast.LENGTH_SHORT).show()
        // TODO: сделать диалоговое окно редактировать и удалить 
    }
}

