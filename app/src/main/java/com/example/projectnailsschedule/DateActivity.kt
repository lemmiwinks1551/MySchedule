package com.example.projectnailsschedule

import android.app.Dialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class DateActivity : AppCompatActivity() {
    companion object {
        const val COLUMN_START = DatabaseHelper.COLUMN_START
        const val COLUMN_PROCEDURE = DatabaseHelper.COLUMN_PROCEDURE
        const val COLUMN_NAME = DatabaseHelper.COLUMN_NAME
        const val COLUMN_PHONE = DatabaseHelper.COLUMN_PHONE
        const val COLUMN_MISC = DatabaseHelper.COLUMN_MISC
    }

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
        // TODO: Выбирать, только выбранную дату из БД

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        val headers =
            arrayOf(COLUMN_START, COLUMN_PROCEDURE, COLUMN_NAME, COLUMN_PHONE, COLUMN_MISC)

        val receiver = intArrayOf(
            R.id.COLUMN_START,
            R.id.COLUMN_END,
            R.id.COLUMN_NAME,
            R.id.COLUMN_PHONE,
            R.id.COLUMN_MISC
        )

        // Создаем адаптер, передаем в него курсор
        adapter = SimpleCursorAdapter(
            this, R.layout.database,
            cursor, headers, receiver, 0
        )

        scheduleList!!.adapter = adapter

        scheduleList!!.onItemLongClickListener = OnItemLongClickListener { arg0, arg1, pos, id ->

            Log.v("scheduleList", "Pressed: $pos")
            Toast.makeText(this, "нажатие", Toast.LENGTH_SHORT).show()
            showDialog()
            true

        }
    }

    fun showDialog() {
        // TODO: Вынести в отдельный класс 
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_db)
        dialog.show()
    }

    public override fun onDestroy() {
        super.onDestroy()
        // Закрываем подключение и курсор
        Log.e("Database", "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }


}

