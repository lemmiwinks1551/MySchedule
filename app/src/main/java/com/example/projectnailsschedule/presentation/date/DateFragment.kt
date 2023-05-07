package com.example.projectnailsschedule.presentation.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentDateBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.date.dateRecyclerView.DateAdapter
import com.example.projectnailsschedule.util.Util


class DateFragment : Fragment(), DateAdapter.OnItemListener {
    val log = this::class.simpleName

    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    private val bindingKey = "dateParams"
    private val bindingKeyAppointment = "appointmentParams"
    private val noAppointmentsTextTitle = "Нет записей"
    private val dateRecyclerViewSpanCount = 1

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

        return binding.root
    }

    private fun initWidgets() {
        dateRecyclerView = binding.scheduleRecyclerView
    }

    private fun initClickListeners() {
        // add new appointment
        binding.addButton.setOnClickListener {
            val appointmentParams = AppointmentModelDb(
                _id = null,
                date = Util().dateConverterNew(dateParams?.date.toString()),
                name = null,
                time = null,
                procedure = null,
                phone = null,
                notes = null,
                deleted = false
            )
            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)
            it.findNavController().navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }
    }

    private fun setObservers() {
        // dateParams observer
        dateViewModel?.selectedDateParams?.observe(viewLifecycleOwner) {
            if (it.appointmentCount != 0) {
                inflateDateRecyclerView(it)
            } else {
                binding.fragmentDateTitle.text = noAppointmentsTextTitle
            }
            binding.fragmentDateDate.text = it.date?.format(Util().formatter)
        }
    }

    private fun inflateDateRecyclerView(selectedDate: DateParams) {
        // create adapter
        val dateAdapter = DateAdapter(
            appointmentsCount = selectedDate.appointmentCount!!,
            onItemListener = this,
            dateViewModel = dateViewModel!!,
            fragmentActivity = requireActivity()
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, dateRecyclerViewSpanCount)

        dateRecyclerView?.layoutManager = layoutManager
        dateRecyclerView?.adapter = dateAdapter
        dateRecyclerView?.scheduleLayoutAnimation()
    }

    override fun onResume() {
        super.onResume()
        // hide keyboard
        Util().hideKeyboard(requireActivity())
    }

    override fun onItemClick(position: Int) {
        // not implemented yet
    }
}

