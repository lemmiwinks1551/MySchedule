package com.example.projectnailsschedule.ui.search

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FilterQueryProvider
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.dbHelpers.ScheduleDbHelper


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    lateinit var userList: ListView
    private lateinit var userFilter: EditText
    val LOG = this::class.simpleName


    private var scheduleDbHelper: ScheduleDbHelper? = null
    private var db: SQLiteDatabase? = null
    private var userCursor: Cursor? = null
    private var userAdapter: SimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleDbHelper = ScheduleDbHelper(context)
        scheduleDbHelper!!.createDb()
    }

    override fun onResume() {
        super.onResume()
        try {
            db = scheduleDbHelper?.open()
            userCursor = db?.rawQuery("SELECT * FROM ${ScheduleDbHelper.TABLE_NAME}", null)

            // Инициируем список заголовков и плейсхолдеры для них
            val headers = arrayOf(ScheduleDbHelper.COLUMN_NAME, ScheduleDbHelper.COLUMN_DATE)
            val headersInt = intArrayOf(android.R.id.text1, android.R.id.text2)

            userAdapter = SimpleCursorAdapter(
                context, android.R.layout.two_line_list_item,
                userCursor, headers, headersInt, 0
            )

            // если в текстовом поле есть текст, выполняем фильтрацию
            userAdapter!!.filter.filter(userFilter.text.toString())

            // установка слушателя изменения текста
            userFilter.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // при изменении текста выполняем фильтрацию
                    userAdapter!!.filter.filter(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            // устанавливаем провайдер фильтрации
            userAdapter!!.filterQueryProvider = object : FilterQueryProvider {
                override fun runQuery(p0: CharSequence?): Cursor {
                    if (p0 == null || p0.isEmpty()) {
                        return db!!.rawQuery("SELECT * FROM ${ScheduleDbHelper.TABLE_NAME}", null)
                    } else {
                        val arr = arrayOf("%$p0%")
                        return db!!.rawQuery(
                            "SELECT * FROM ${ScheduleDbHelper.TABLE_NAME} WHERE ${ScheduleDbHelper.COLUMN_NAME} LIKE ?",
                            arr
                        )
                    }
                }
            }
            userList.adapter = userAdapter;
        } catch (e: SQLException) {
            Log.e(LOG, e.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val serviceViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Создаем базу данных
        scheduleDbHelper?.createDb()

        // Определить View
        userList = binding.userList
        userFilter = binding.userFilter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        userCursor?.close()
        db?.close()
    }
}