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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAppointmentBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.presentation.calendar.DateParamsViewModel
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    private val dateParamsViewModel: DateParamsViewModel by activityViewModels()
    private val clientsViewModel: ClientsViewModel by activityViewModels()

    private lateinit var currentAppointment: AppointmentModelDb

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var saveToolbarButton: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        defineAppointment()

        // set current appointmentParams
        inflateViews()

        // set title text
        setTitle()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("client")
            ?.observe(viewLifecycleOwner) {
                clientSelected()
            }

        // select procedure results
        // TODO: переписать через вью модель
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProcedureModelDb>("procedure")
            ?.observe(viewLifecycleOwner) { savedState ->
                procedureSelected(savedState)
            }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // add save button
        saveToolbarButton = menu.findItem(R.id.save_toolbar_button)
        saveToolbarButton.isVisible = true

        setClickListeners()
        super.onPrepareOptionsMenu(menu)
    }

    private fun setClickListeners() {
        saveToolbarButton.setOnMenuItemClickListener {
            if (currentAppointment._id == null) {
                // new Appointment
                createAppointment()
            } else {
                // edit Appointment
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
        val date: TextView = binding.fragmentAppointmentDate
        date.text = Util().formatDateToRus(dateParamsViewModel.selectedDate.value?.date!!)

        if (currentAppointment._id == null) {
            // no _id - add new Appointment
            title.text = getString(R.string.new_appointment_text)
        } else {
            // _id - edit Appointment
            title.text = getString(R.string.change_appointment_text)
        }
    }

    private fun createAppointment() {
        // create appointmentParams object
        with(binding) {
            currentAppointment = AppointmentModelDb(
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
                photo = clientsViewModel.selectedClient?.photo,
                deleted = false
            )

            lifecycleScope.launch {
                currentAppointment._id = dateParamsViewModel.insertAppointment(currentAppointment)
            }

            val toast: Toast = Toast.makeText(
                context,
                "${currentAppointment.date}\n${getString(R.string.toast_created)}",
                Toast.LENGTH_LONG
            )
            toast.show()
        }

        // Return to previous screen
        findNavController().popBackStack()
    }

    private fun editAppointment() {
        // create appointmentParams object
        with(binding) {
            currentAppointment = AppointmentModelDb(
                _id = currentAppointment._id,
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
                photo = clientsViewModel.selectedClient?.photo,
                deleted = false
            )

            lifecycleScope.launch {
                dateParamsViewModel.updateAppointment(currentAppointment)
            }

            Toast.makeText(context, getString(R.string.toast_edited), Toast.LENGTH_LONG).show()

            findNavController().popBackStack()
        }
    }

    private fun inflateViews() {
        // if appointment position != null - inflate view
        if (dateParamsViewModel.appointmentPosition != null) {
            with(currentAppointment) {
                binding.dayEditText.text = this.date
                binding.timeEditText.text = this.time
                binding.procedureEt.setText(this.procedure)
                binding.nameEt.setText(this.name)
                binding.phoneEt.setText(this.phone)
                binding.clientVkLinkEt.setText(this.vk)
                binding.clientTelegramLinkEt.setText(this.telegram)
                binding.clientInstagramLinkEt.setText(this.instagram)
                binding.clientWhatsappLinkEt.setText(this.whatsapp)
                binding.clientAvatarDateAppointment.setImageURI(this.photo?.toUri())
                binding.notesEt.setText(this.notes)
            }
        } else {
            binding.dayEditText.text =
                Util().formatDateToRus(dateParamsViewModel.selectedDate.value?.date!!)
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

    private fun defineAppointment() {
        // set current appointmentParams
        currentAppointment = if (dateParamsViewModel.appointmentPosition != null) {
            // if AppointmentModelDb already exists
            dateParamsViewModel.selectedDate.value!!.appointmentsList!![dateParamsViewModel.appointmentPosition!!]
        } else {
            // new AppointmentModelDb
            AppointmentModelDb(deleted = false)
        }
    }

    private fun clientSelected() {
        // update views
        with(binding) {
            nameEt.setText(clientsViewModel.selectedClient!!.name)
            phoneEt.setText(clientsViewModel.selectedClient!!.phone)
            clientVkLinkEt.setText(clientsViewModel.selectedClient!!.vk)
            clientTelegramLinkEt.setText(clientsViewModel.selectedClient!!.telegram)
            clientInstagramLinkEt.setText(clientsViewModel.selectedClient!!.instagram)
            clientWhatsappLinkEt.setText(clientsViewModel.selectedClient!!.whatsapp)
            clientAvatarDateAppointment.setImageURI(clientsViewModel.selectedClient!!.photo?.toUri())
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

    private fun procedureSelected(savedState: ProcedureModelDb) {
        // update views
        with(binding) {
            procedureEt.setText(savedState.procedureName)
            Util().animateEditTexts(procedureEt)
        }
    }
}