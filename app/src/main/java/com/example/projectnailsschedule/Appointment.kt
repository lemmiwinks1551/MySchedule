package com.example.projectnailsschedule

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectnailsschedule.databinding.ActivityAppointmentBinding

/**
 * Методы для взаимодействия с записью:
 * Редактировать запись (static), добавить запись
 * */

class Appointment : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)

        // Добавляем ClickListener на кнопки "Добавить" и "Удалить"
        binding.addButton.setOnClickListener {
            addRow()
        }
        binding.cancelButton.setOnClickListener {
            cancelButton()
        }

        // Устанавливаем в редактируемое поле Дата выбранную дату
        if (intent.getStringExtra("appointmentExtra") != null) {
            binding.dayEditText.setText(intent.getStringExtra("appointmentExtra"))
        } else {
            editId()
        }
    }

    private fun addRow() {
        val db: SQLiteDatabase? = databaseHelper.writableDatabase
        val date = binding.dayEditText.text.toString()
        val time = binding.timeEditText.text.toString()
        val procedure = binding.procedureEditText.text.toString()
        val name = binding.nameEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        val misc = binding.miscEditText.text.toString()

        databaseHelper.addRow(date, time, procedure, name, phone, misc, db!!)
        finish()
    }

    private fun editId() {
        val db: SQLiteDatabase? = databaseHelper.writableDatabase
        // Получаем список для заполнения полей из интента
        val extraArray = intent.getStringArrayListExtra("appointmentExtra")

        // Заполняем поля
        with(binding) {
            dayEditText.setText(extraArray!![1])
            timeEditText.setText(extraArray[2])
            procedureEditText.setText(extraArray[3])
            nameEditText.setText(extraArray[4])
            phoneEditText.setText(extraArray[5])
            miscEditText.setText(extraArray[6])
        }
    }

    private fun cancelButton() {
        finish()
    }
}