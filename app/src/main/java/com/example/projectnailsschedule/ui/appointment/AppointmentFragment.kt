package com.example.projectnailsschedule.ui.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.projectnailsschedule.databinding.ActivityAppointmentBinding
import com.example.projectnailsschedule.database.ScheduleDbHelper
import com.example.projectnailsschedule.service.Converter
import java.util.*


/**
 * Методы для взаимодействия с записью:
 * Редактировать запись, добавить запись
 * */

class AppointmentFragment : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var databaseHelper: ScheduleDbHelper
    private lateinit var db: SQLiteDatabase
    private val addTitle = "Добавить"
    private val editTitle = "Редактировать"
    private val date = "date"
    private val time = "time"
    private val procedure = "procedure"
    private val name = "name"
    private val phone = "phone"
    private val misc = "misc"

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        /** Сохранить состояние экрана */
        super.onSaveInstanceState(savedInstanceState)
        with(savedInstanceState) {
            putString(date, binding.dayEditText.text.toString())
            putString(time, binding.timeEditText.text.toString())
            putString(procedure, binding.procedureEditText.text.toString())
            putString(name, binding.nameEditText.text.toString())
            putString(phone, binding.phoneEditText.text.toString())
            putString(misc, binding.miscEditText.text.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = ScheduleDbHelper(applicationContext)
        db = databaseHelper.writableDatabase

        // Добавляем ClickListener на кнопки
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

        // В зависимости от содержания интента выполняем метод "Редактировать"/установить дату
        if (intent.getStringExtra("appointmentExtra") != null) {
            binding.dayEditText.text = intent.getStringExtra("appointmentExtra")
        } else {
            editIdFields()
        }

        // Загружаем значения полей после поворота экрана
        if (savedInstanceState != null) {
            with(binding) {
                dayEditText.text = savedInstanceState.getString(date)
                timeEditText.text = savedInstanceState.getString(time)
                procedureEditText.setText(savedInstanceState.getString(procedure))
                nameEditText.setText(savedInstanceState.getString(name))
                phoneEditText.setText(savedInstanceState.getString(phone))
                miscEditText.setText(savedInstanceState.getString(misc))
            }
        }
    }

    private fun addRow() {
        /** Внести значения полей активности в БД */
        // Собрать данные из полей в переменные и
        val fields = arrayListOf(
            binding.dayEditText.text.toString(),
            binding.timeEditText.text.toString(),
            binding.procedureEditText.text.toString(),
            binding.nameEditText.text.toString(),
            binding.phoneEditText.text.toString(),
            binding.miscEditText.text.toString()
        )
        databaseHelper.addRow(fields, db)
        finish()
    }

    private fun editIdFields() {
        /** Заполнить поля актуальными значениями */
        // Получаем список для заполнения полей из интента
        val extraArray = intent.getStringArrayListExtra("appointmentExtra")

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
        /** Передать в метод БД информацию для обновления */
        // Получаем id строки из интента и передаем
        val id = intent.getStringArrayListExtra("appointmentExtra")?.get(0)?.toString()
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
        finish()
    }

    private fun cancelButton() {
        /** Кнопка Отмены */
        finish()
    }

    private fun selectDate() {
        /** Устанавливает выбор даты на поле Дата */
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, pickedYear, pickedMonth, pickedDay ->
                val date = Converter().dateConverter("$pickedDay.${pickedMonth + 1}.$pickedYear")
                binding.dayEditText.text = date
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun selectTime() {
        /** Устанавливает выбор времени на поле Время */
        val calendar = Calendar.getInstance()
        val mTimePicker: TimePickerDialog
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            this, { _, pickedHour, pickedMinute ->
                //val time = "$pickedHour:$pickedMinute"
                val time = String.format("%02d:%02d", pickedHour, pickedMinute)
                binding.timeEditText.text = time
            }, hour, minute, true
        )
        mTimePicker.show()
    }
}