package com.example.projectnailsschedule

import android.annotation.SuppressLint
import android.app.Dialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat


class DateActivity : AppCompatActivity() {
    companion object {
        const val COLUMN_START = DatabaseHelper.COLUMN_START
        const val COLUMN_PROCEDURE = DatabaseHelper.COLUMN_PROCEDURE
        const val COLUMN_NAME = DatabaseHelper.COLUMN_NAME
        const val COLUMN_PHONE = DatabaseHelper.COLUMN_PHONE
        const val COLUMN_MISC = DatabaseHelper.COLUMN_MISC
        const val COLUMN_DATE = DatabaseHelper.COLUMN_DATE
    }

    private var scheduleList: ListView? = null
    private var databaseHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private var cursor: Cursor? = null
    private var adapter: SimpleCursorAdapter? = null
    private var deleteButton: Button? = null
    private var editButton: Button? = null
    private var day: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // Получаем интент с датой
        day = intent.getStringExtra("day").toString()

        // Конверитуем дату в удобный формат
        day = dateConverter(day!!)

        // Устанавливаем в Toolbar дату
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = day
        setSupportActionBar(toolbar)

        scheduleList = findViewById(R.id.scheduleListView)
        databaseHelper = DatabaseHelper(applicationContext)
    }

    public override fun onResume() {
        super.onResume()
        // Получаем строку из БД
        getDbQuery()
        // Устанавливаем полученную строку в ListView
        scheduleList!!.adapter = adapter

        // Добавляем ClickListener к ListView расписания
        scheduleList!!.onItemLongClickListener = OnItemLongClickListener { arg0, arg1, pos, id ->

            Log.e("scheduleList", "Pressed: $pos")
            // Выводим диалоговое окно на экран
            showDialog()
            true

        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_db)
        deleteButton = dialog.findViewById(R.id.delete)
        editButton = dialog.findViewById(R.id.edit)

        Log.e("DialogDB", "Created")

        deleteButton?.setOnClickListener {
            Log.e("DialogDB", "Delete")
            dialog.dismiss()
        }

        editButton?.setOnClickListener {
            Log.e("DialogDB", "Edit")
            dialog.dismiss()
        }

        Log.e("DialogDB", "Shown")

        dialog.show()
    }

    private fun getDbQuery() {
        // Функция получает из БД строчку в зависимости от дня записи.
        Log.e("Database", "Открываем подключение")

        // Окрываем подключение
        db = databaseHelper?.readableDatabase

        // Формируем запрос к БД
        //val query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + COLUMN_DATE + " = '$day';"
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE $COLUMN_DATE = '$day';"

        // Получаем данные из бд в виде курсора
        cursor = db!!.rawQuery(query, null)

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        val headers =
            arrayOf(COLUMN_START, COLUMN_PROCEDURE, COLUMN_NAME, COLUMN_PHONE, COLUMN_MISC)
        // Определяем список элементов, которые будут заполнять
        val receiver = intArrayOf(
            R.id.COLUMN_START,
            R.id.COLUMN_PROCEDURE,
            R.id.COLUMN_NAME,
            R.id.COLUMN_PHONE,
            R.id.COLUMN_MISC
        )

        // Создаем адаптер, передаем в него курсор
        adapter = SimpleCursorAdapter(
            this, R.layout.database,
            cursor, headers, receiver, 0
        )
    }

    public override fun onDestroy() {
        super.onDestroy()
        // Закрываем подключение и курсор
        Log.e("Database", "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateConverter(day: String): String {
        // Получаем день из интента в формате d.M.yyyy и конвертируем в формат dd.MM.yyyy
        val parser = SimpleDateFormat("d.M.yyyy")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(day)).toString()
    }
}

