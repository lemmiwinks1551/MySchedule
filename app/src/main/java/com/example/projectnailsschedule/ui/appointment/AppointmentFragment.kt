package com.example.projectnailsschedule.ui.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.MainActivity
import com.example.projectnailsschedule.database.ScheduleDbHelper
import com.example.projectnailsschedule.databinding.FragmentAppointmentBinding
import com.example.projectnailsschedule.service.Converter
import com.example.projectnailsschedule.ui.calendar.CalendarFragment
import com.example.projectnailsschedule.ui.date.DateViewModel
import java.util.*


/**
 * Методы для взаимодействия с записью:
 * Редактировать запись, добавить запись
 * */

class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: ScheduleDbHelper
    private lateinit var db: SQLiteDatabase
    val LOG = this::class.simpleName
    private val addTitle = "Добавить"
    private val editTitle = "Редактировать"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appointmentViewModel =
            ViewModelProvider(this)[DateViewModel::class.java]

        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        databaseHelper = ScheduleDbHelper(context)
        db = databaseHelper.writableDatabase

        // Set ClickListener
        binding.addEditButton.setOnClickListener {
            when (binding.addEditButton.text.toString()) {
                // Если на кнопке написано Добавить - вызвать метод по добавлению строки
                addTitle -> addRow()
                // Если на кнопке написано Редактировать - вызвать метод по редактированию строки
                editTitle -> editIdQuery()
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
        if (arguments?.getStringArrayList("appointmentExtra") != null) {
            editIdFields()
        } else {
            binding.dayEditText.text = arguments?.getString("date")
            Log.e(LOG, "No Arguments in bundle")
        }

        return binding.root
    }

    private fun addRow() {
        // Add a row in database
        // Create an array of data
        val fields = arrayListOf(
            binding.dayEditText.text.toString(),
            binding.timeEditText.text.toString(),
            binding.procedureEditText.text.toString(),
            binding.nameEditText.text.toString(),
            binding.phoneEditText.text.toString(),
            binding.miscEditText.text.toString()
        )
        databaseHelper.addRow(fields, db)

        // Return to previous screen
        findNavController().popBackStack()
    }

    private fun editIdFields() {
        // Заполнить поля актуальными значениями
        // Получаем список для заполнения полей из интента

        val extraArray = arguments?.getStringArrayList("appointmentExtra")
        binding.title.text = "Редактировать запись"

        // Устанавливаем актуальные значения в поля для редактирования
        with(binding) {
            dayEditText.text = extraArray!![1]
            timeEditText.text = extraArray[2]
            procedureEditText.setText(extraArray[3])
            nameEditText.setText(extraArray[4])
            phoneEditText.setText(extraArray[5])
            miscEditText.setText(extraArray[6])
            addEditButton.text = "Редактировать"
        }
    }

    private fun editIdQuery() {
        // Передать в метод БД информацию для обновления
        // Получаем id строки из интента и передаем
        val id = arguments?.getStringArrayList("appointmentExtra")?.get(0)?.toString()

        val extraArrayQuery = arrayListOf(
            id!!,
            binding.dayEditText.text.toString(),
            binding.timeEditText.text.toString(),
            binding.procedureEditText.text.toString(),
            binding.nameEditText.text.toString(),
            binding.phoneEditText.text.toString(),
            binding.miscEditText.text.toString(),
        )
        databaseHelper.editId(extraArrayQuery, db)
        findNavController().popBackStack()
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
                val date = Converter().dateConverter("$pickedDay.${pickedMonth + 1}.$pickedYear")
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