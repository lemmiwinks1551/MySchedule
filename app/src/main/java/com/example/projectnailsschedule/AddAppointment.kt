package com.example.projectnailsschedule

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectnailsschedule.databinding.ActivityAddAppointmentBinding

class AddAppointment : AppCompatActivity() {

    private lateinit var binding: ActivityAddAppointmentBinding
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)

        // Добавляем ClickListener на кнопки "Добавить" и "Редактировать"
        binding.addButton.setOnClickListener {
            addRow()
        }
        binding.cancelButton.setOnClickListener {
            cancelButton()
        }

        // Устанавливаем в редактируемое поле Дата выбранную дату
        binding.dayEditText.setText(intent.getStringExtra("date"))

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

    }

    private fun cancelButton() {
        finish()
    }

}