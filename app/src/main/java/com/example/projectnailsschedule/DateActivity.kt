package com.example.projectnailsschedule

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity

class DateActivity : AppCompatActivity() {
    var scheduleList: ListView? = null
    var databaseHelper: DatabaseHelper? = null
    var db: SQLiteDatabase? = null
    var cursor: Cursor? = null
    var adapter: SimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // Получаем интент с днем
        val day = intent.getStringExtra("day").toString()
        val outputDay = String.format("Расписание $day")

        // Устанавливаем в Toolbar дату
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = outputDay
        setSupportActionBar(toolbar)

        scheduleList = findViewById(R.id.scheduleListView)
        databaseHelper = DatabaseHelper(applicationContext)
    }

    public override fun onResume() {
        super.onResume()
        Log.e("Database", "Открываем подключение")

        // Окрываем подключение
        db = databaseHelper?.readableDatabase

        // Получаем данные из бд в виде курсора
        cursor = db!!.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null)

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        val headers = arrayOf(
            DatabaseHelper.COLUMN_START, DatabaseHelper.COLUMN_END,
            DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_PHONE,
            DatabaseHelper.COLUMN_MISC
        )

        // Создаем адаптер, передаем в него курсор
        adapter = SimpleCursorAdapter(
            this, android.R.layout.two_line_list_item,
            cursor, headers, intArrayOf(android.R.id.text1, android.R.id.text2), 0
            // TODO: переделать отображение БД 
        )

        scheduleList!!.adapter = adapter
    }

    public override fun onDestroy() {
        super.onDestroy()
        // Закрываем подключение и курсор
        Log.e("Database", "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }
}
