package com.example.projectnailsschedule.presentation.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAppointmentBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.presentation.appointment.selectClient.SelectClientFragment
import com.example.projectnailsschedule.presentation.appointment.selectProcedure.SelectProcedureFragment
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    val log = this::class.simpleName
    private val bindingKey = "appointmentParams"

    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var saveToolbarButton: MenuItem

    private var appointmentParams: AppointmentModelDb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get appointmentParams from arguments
        appointmentParams = arguments?.getParcelable(bindingKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // set binding
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        // set current appointmentParams form DateFragment binding object
        setAppointmentCurrentParams()

        // set title text
        setTitle()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ClientModelDb>("client")
            ?.observe(viewLifecycleOwner) { result ->
                // здесь обрабатываем результат после выбора клиента
                with(binding) {
                    nameEt.setText(result.name)
                    phoneEt.setText(result.phone)
                    clientVkLinkEt.setText(result.vk)
                    clientTelegramLinkEt.setText(result.telegram)
                    clientInstagramLinkEt.setText(result.instagram)
                    clientWhatsappLinkEt.setText(result.whatsapp)
                    Util().animateEditTexts(
                        nameEt,
                        phoneEt,
                        clientVkLinkEt,
                        clientTelegramLinkEt,
                        clientInstagramLinkEt,
                        clientWhatsappLinkEt
                    )
                }
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProcedureModelDb>("procedure")
            ?.observe(viewLifecycleOwner) { result ->
                with(binding) {
                    procedureEt.setText(result.procedureName)
                    Util().animateEditTexts(procedureEt)
                }
            }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        saveToolbarButton = menu.findItem(R.id.save_toolbar_button)
        saveToolbarButton.isVisible = true
        setClickListeners()
        super.onPrepareOptionsMenu(menu)
    }

    private fun setClickListeners() {
        saveToolbarButton.setOnMenuItemClickListener {
            if (appointmentParams?._id == null) {
                // no _id - add new Appointment
                createAppointment()
            } else {
                // _id - edit Appointment
                editAppointment()
            }
            true
        }
        with(binding) {
            // set ClickListener on day_edit_text
            dayEditText.setOnClickListener {
                setDatePicker()
            }

            // set ClickListener on time_edit_text
            timeEditText.setOnClickListener {
                setTimePicker()
            }

            selectClientButton.setOnClickListener {
                val dialogFragment = SelectClientFragment()
                dialogFragment.show(parentFragmentManager, "SelectClientFragment")
            }

            procedureSelectButton.setOnClickListener {
                val dialogFragment = SelectProcedureFragment()
                dialogFragment.show(parentFragmentManager, "SelectProcedureFragment")
            }
        }
    }

    private fun setTitle() {
        val title: TextView = binding.fragmentAppointmentTitle
        val titleDate: TextView = binding.fragmentAppointmentDate

        if (appointmentParams?._id == null) {
            // no _id - add new Appointment
            title.text = getString(R.string.new_appointment_text)
        } else {
            // _id - edit Appointment
            title.text = getString(R.string.change_appointment_text)
        }
        titleDate.text = appointmentParams?.date
    }

    private fun createAppointment() {
        /** Send params to ViewModel */

        // create appointmentParams object
        with(binding) {
            val appointmentModelDb = AppointmentModelDb(
                _id = null,
                date = dayEditText.text.toString(),
                name = nameEt.text.toString(),
                time = timeEditText.text.toString(),
                procedure = procedureEt.text.toString(),
                phone = phoneEt.text.toString(),
                vk = clientVkLinkEt.text.toString(),
                telegram = clientTelegramLinkEt.text.toString(),
                instagram = clientInstagramLinkEt.text.toString(),
                whatsapp = clientWhatsappLinkEt.text.toString(),
                notes = notesEt.text.toString(),
                deleted = false
            )
            appointmentViewModel.insertAppointment(appointmentModelDb)

            val toast: Toast = Toast.makeText(
                context,
                "${appointmentModelDb.date}\n${getString(R.string.toast_created)}",
                Toast.LENGTH_LONG
            )
            toast.show()
        }

        // Return to previous screen
        findNavController().popBackStack()
    }

    private fun editAppointment() {
        /** Send params to ViewModel */

        // create appointmentParams object
        with(binding) {
            val appointmentModelDb = AppointmentModelDb(
                _id = appointmentParams?._id,
                date = dayEditText.text.toString(),
                name = nameEt.text.toString(),
                time = timeEditText.text.toString(),
                procedure = procedureEt.text.toString(),
                phone = phoneEt.text.toString(),
                vk = clientVkLinkEt.text.toString(),
                telegram = clientTelegramLinkEt.text.toString(),
                instagram = clientInstagramLinkEt.text.toString(),
                whatsapp = clientWhatsappLinkEt.text.toString(),
                notes = notesEt.text.toString(),
                deleted = false
            )

            // send to AppointmentViewModel
            appointmentViewModel.updateAppointment(appointmentModelDb)

            Toast.makeText(context, getString(R.string.toast_edited), Toast.LENGTH_LONG).show()

            findNavController().popBackStack()
        }
    }

    private fun setAppointmentCurrentParams() {
        // set current appointmentParams from DateFragment binding object

        with(binding) {
            dayEditText.text = appointmentParams?.date
            timeEditText.text = appointmentParams?.time
            procedureEt.setText(appointmentParams?.procedure)
            nameEt.setText(appointmentParams?.name)
            phoneEt.setText(appointmentParams?.phone)
            clientVkLinkEt.setText(appointmentParams?.vk)
            clientTelegramLinkEt.setText(appointmentParams?.telegram)
            clientInstagramLinkEt.setText(appointmentParams?.instagram)
            clientWhatsappLinkEt.setText(appointmentParams?.whatsapp)
            notesEt.setText(appointmentParams?.notes)
        }
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
        // clocks time picker
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

    private fun setSpinnerTimePicker() {
        // spinner time picker
        val myCalender = Calendar.getInstance()
        val hour = myCalender[Calendar.HOUR_OF_DAY]
        val minute = myCalender[Calendar.MINUTE]
        val myTimeListener =
            OnTimeSetListener { view, hourOfDay, minute ->
                if (view.isShown) {
                    myCalender[Calendar.HOUR_OF_DAY] = hourOfDay
                    myCalender[Calendar.MINUTE] = minute
                }
            }
        val timePickerDialog = TimePickerDialog(
            activity,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myTimeListener,
            hour,
            minute,
            true
        )
        timePickerDialog.setTitle("Choose hour:")
        timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}