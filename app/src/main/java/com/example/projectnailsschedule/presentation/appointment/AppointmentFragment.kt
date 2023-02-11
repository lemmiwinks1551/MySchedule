package com.example.projectnailsschedule.presentation.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.databinding.FragmentAppointmentBinding
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.util.Util
import java.util.*

/** AppointmentFragment View*/

class AppointmentFragment : Fragment() {
    val log = this::class.simpleName
    private val bindingKey = "appointmentParams"
    private val toastCreated = "Запись добавлена"
    private val toastEdited = "Запись изменена"

    private var appointmentViewModel: AppointmentViewModel? = null
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private var appointmentParams: AppointmentParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        appointmentViewModel = ViewModelProvider(
            this,
            AppointmentViewModelFactory(context)
        )[AppointmentViewModel::class.java]

        // get appointmentParams from arguments
        appointmentParams = arguments?.getParcelable(bindingKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // set binding
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        // Set ClickListener on save_changes_button
        binding.saveChangesButton.setOnClickListener {
            if (appointmentParams?._id == null) {
                // no _id - add new Appointment
                createAppointment()
            } else {
                // _id - edit Appointment
                editAppointment()
            }
        }

        // set ClickListeners
        setClickListeners()

        // set current appointmentParams form DateFragment binding object
        setAppointmentCurrentParams()

        return binding.root
    }

    private fun setClickListeners() {
        // set ClickListener on cancel_button
        binding.cancelButton.setOnClickListener {
            cancelButton()
        }

        // set ClickListener on day_edit_text
        binding.dayEditText.setOnClickListener {
            setDatePicker()
        }

        // set ClickListener on time_edit_text
        binding.timeEditText.setOnClickListener {
            setTimePicker()
        }

        // set phone input format on phone_edit_text
        binding.phoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    private fun createAppointment() {
        /** Send params to ViewModel */

        // create appointmentParams object
        with(binding) {
            val appointmentParams = AppointmentParams(
                appointmentDate = dayEditText.text.toString(),
                clientName = nameEditText.text.toString(),
                startTime = timeEditText.text.toString(),
                procedureName = procedureEditText.text.toString(),
                phoneNum = phoneEditText.text.toString(),
                misc = miscEditText.text.toString()
            )
            appointmentViewModel?.createAppointment(appointmentParams)

            Toast.makeText(
                context,
                "$toastCreated ${appointmentParams.appointmentDate}",
                Toast.LENGTH_LONG
            ).show()
        }

        // Return to previous screen
        findNavController().popBackStack()
    }

    private fun editAppointment() {
        /** Send params to ViewModel */

        // create appointmentParams object
        with(binding) {
            val appointmentParams = AppointmentParams(
                _id = appointmentParams?._id,
                appointmentDate = dayEditText.text.toString(),
                clientName = nameEditText.text.toString(),
                startTime = timeEditText.text.toString(),
                procedureName = procedureEditText.text.toString(),
                phoneNum = phoneEditText.text.toString(),
                misc = miscEditText.text.toString()
            )

            // send to AppointmentViewModel
            appointmentViewModel?.editAppointment(appointmentParams)

            Toast.makeText(
                context,
                "$toastEdited ${appointmentParams.appointmentDate}",
                Toast.LENGTH_LONG
            ).show()

            findNavController().popBackStack()
        }
    }

    private fun setAppointmentCurrentParams() {
        // set current appointmentParams from DateFragment binding object

        with(binding) {
            dayEditText.text = appointmentParams?.appointmentDate
            timeEditText.text = appointmentParams?.startTime
            procedureEditText.setText(appointmentParams?.procedureName)
            nameEditText.setText(appointmentParams?.clientName)
            phoneEditText.setText(appointmentParams?.phoneNum)
            miscEditText.setText(appointmentParams?.misc)
        }
    }

    private fun cancelButton() {
        // Cancel button
        findNavController().popBackStack()
    }

    private fun setDatePicker() {
        // set datePicker to select date field
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, pickedYear, pickedMonth, pickedDay ->
                val date = Util().dateConverter("$pickedDay.${pickedMonth + 1}.$pickedYear")
                binding.dayEditText.text = date
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun setTimePicker() {
        // set time Picker to select time field
        val calendar = Calendar.getInstance()
        val mTimePicker: TimePickerDialog
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            context, { _, pickedHour, pickedMinute ->
                val time = String.format("%02d:%02d", pickedHour, pickedMinute)
                binding.timeEditText.text = time
            }, hour, minute, true
        )
        mTimePicker.show()
    }
}