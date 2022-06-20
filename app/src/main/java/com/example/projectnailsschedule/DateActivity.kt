package com.example.projectnailsschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar

class DateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // Получаем интент
        val day = intent.getStringExtra("day").toString()
        val outputDay = String.format("Расписание $day")

        // Устанавливаем Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = outputDay
        setSupportActionBar(toolbar)

    }
}