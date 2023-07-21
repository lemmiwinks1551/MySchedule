package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentFullMonthViewBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV.FullMonthViewRVAdapter
import com.example.projectnailsschedule.util.Util
import java.time.LocalDate

class FullMonthViewFragment : Fragment() {
    val log = this::class.simpleName

    private var fullMonthViewVM: FullMonthViewViewModel? = null

    private var _binding: FragmentFullMonthViewBinding? = null
    private var fullMonthAppointmentsRV: RecyclerView? = null
    private var appointmentsRVAdapter: FullMonthViewRVAdapter? = null
    private var appointmentList: List<AppointmentModelDb>? = null
    private val dateRecyclerViewSpanCount = 1
    private val bindingKeyAppointment = "appointmentParams"

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

        return binding.root
    }

    private fun initWidgets() {
        fullMonthAppointmentsRV = binding.fullMonthAppointmentsRV
    }

    private fun setObservers() {
        val dateMonth = ".07.2023"
        val dateMonthQuery = "%$dateMonth%"

        fullMonthViewVM!!.getMonthAppointments(dateMonthQuery).observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                inflateAppointmentsRV(list)
                appointmentList = list
            }
        }
    }

    private fun inflateAppointmentsRV(appointmentsList: List<AppointmentModelDb>) {
        // create adapter
        appointmentsRVAdapter = FullMonthViewRVAdapter(
            appointmentsCount = appointmentsList.size,
            appointmentsList = appointmentsList,
            context = requireContext()
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, dateRecyclerViewSpanCount)

        fullMonthAppointmentsRV?.layoutManager = layoutManager
        fullMonthAppointmentsRV?.adapter = appointmentsRVAdapter
        fullMonthAppointmentsRV?.scheduleLayoutAnimation()

        // set clickListener on dateRV
        appointmentsRVAdapter!!.setOnItemClickListener(object :
            FullMonthViewRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected appointment
                val bundle = Bundle()
                val appointmentModelDb = AppointmentModelDb(
                    _id = appointmentList?.get(position)?._id,
                    date = appointmentList?.get(position)?.date,
                    name = appointmentList?.get(position)?.name,
                    time = appointmentList?.get(position)?.time,
                    procedure = appointmentList?.get(position)?.procedure,
                    phone = appointmentList?.get(position)?.phone,
                    notes = appointmentList?.get(position)?.notes,
                    deleted = appointmentList?.get(position)!!.deleted
                )
                val navController = findNavController()
                bundle.putParcelable(bindingKeyAppointment, appointmentModelDb)
                navController.navigate(R.id.action_fullMonthViewFragment_to_nav_appointment, bundle)
            }
        })
    }

}