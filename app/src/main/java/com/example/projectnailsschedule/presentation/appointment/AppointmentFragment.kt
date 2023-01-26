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
import com.example.projectnailsschedule.util.Service
import java.util.*

/**
 * Методы для взаимодействия с записью:
 * Редактировать запись, добавить запись */

class AppointmentFragment : Fragment() {
    val log = this::class.simpleName

    private var appointmentViewModel: AppointmentViewModel? = null
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private var appointmentParams: AppointmentParams? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appointmentViewModel = ViewModelProvider(
            this,
            AppointmentViewModelFactory(context)
        )[AppointmentViewModel::class.java]

        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        appointmentParams = arguments?.getParcelable("appointmentParams")

        // True - add new Appointment
        // False - Edit Appointment
        val callIntent: Boolean = appointmentParams?._id == null

        // Set ClickListener
        binding.addEditButton.setOnClickListener {
            if (callIntent) {
                saveAppointment()
            } else {
                editAppointment()
            }

        }
        binding.cancelButton.setOnClickListener {
            cancelButton()
        }
        binding.dayEditText.setOnClickListener {
            selectDate()
        }
        binding.timeEditText.setOnClickListener {
            selectTime()
        }

        // Добавляем формат ввода на поле "Телефон"
        binding.phoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // В зависимости от содержания интента выполняем метод "Редактировать"/"Установить дату"
        setAppointmentCurrentParams()

        return binding.root
    }

    private fun saveAppointment() {
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
            appointmentViewModel?.saveAppointment(appointmentParams)

            Toast.makeText(
                context,
                "Запись добавлена ${dayEditText.text}",
                Toast.LENGTH_LONG
            ).show()
        }

        // Return to previous screen
        findNavController().popBackStack()
    }

    private fun editAppointment() {
        /** Send params to ViewModel */
        // get appointment _id from arguments
        val id = appointmentParams?._id

        // create appointmentParams object
        with(binding) {
            val appointmentParams = AppointmentParams(
                _id = id,
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
                "Запись изменена ${dayEditText.text}",
                Toast.LENGTH_LONG
            ).show()

            findNavController().popBackStack()
        }
    }

    private fun setAppointmentCurrentParams() {
        // Заполнить поля актуальными значениями
        // Получаем список для заполнения полей из интента

        val appointmentParams: AppointmentParams? = arguments?.getParcelable("appointmentParams")

        // Устанавливаем актуальные значения в поля для редактирования
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

    private fun selectDate() {
        // Устанавливает выбор даты на поле Дата
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, pickedYear, pickedMonth, pickedDay ->
                val date = Service().dateConverter("$pickedDay.${pickedMonth + 1}.$pickedYear")
                binding.dayEditText.text = date
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun selectTime() {
        //** Устанавлива ет выбор времени на поле Время *//*
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