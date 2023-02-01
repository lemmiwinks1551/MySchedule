package com.example.projectnailsschedule.presentation.search

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSearchBinding
import com.example.projectnailsschedule.data.storage.ScheduleDbHelper
import com.example.projectnailsschedule.util.Service
import com.example.projectnailsschedule.presentation.calendar.CalendarFragment
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var userList: ListView
    private lateinit var userSearch: EditText
    val LOG = this::class.simpleName

    private var scheduleDbHelper: ScheduleDbHelper? = null
    private var db: SQLiteDatabase? = null
    private var userCursor: Cursor? = null
    private var userAdapter: SimpleCursorAdapter? = null
    private var toggleButton: ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleDbHelper = ScheduleDbHelper(context)
        scheduleDbHelper!!.createDb()
    }

    override fun onResume() {
        super.onResume()

        userList.onItemClickListener = OnItemClickListener { arg0, arg1, position, arg3 ->
            val date = (arg0.adapter as SimpleCursorAdapter).cursor.getString(1)
            val bundle = Bundle()
            bundle.putString("date", date)
            // TODO: ??? !!! клик по полю в поиске
/*           CalendarViewModel.day = Service().stringToLocalDate(date).dayOfMonth.toString()
            CalendarViewModel.month = Service().stringToLocalDate(date).monthValue.toString()
            CalendarViewModel.year = Service().stringToLocalDate(date).year.toString()*/
            findNavController().navigate(R.id.action_nav_search_to_nav_date, bundle)
        }

        toggleButton?.setOnCheckedChangeListener { _, b ->
            if (b) {
                userSearch.inputType = InputType.TYPE_CLASS_TEXT
                userSearch.text.clear()
                Log.e(LOG, "Поиск по имени")
            } else {
                userSearch.inputType = InputType.TYPE_CLASS_PHONE
                userSearch.addTextChangedListener(PhoneNumberFormattingTextWatcher())
                userSearch.text.clear()
                Log.e(LOG, "Поиск по телефону")
            }
        }


        // TODO: implement Filter method
        try {
            db = scheduleDbHelper?.open()
            userCursor = db?.rawQuery(
                "SELECT * FROM ${ScheduleDbHelper.TABLE_NAME} ORDER BY ${ScheduleDbHelper.COLUMN_DATE} DESC",
                null
            )

            // Инициируем список заголовков и плейсхолдеры для них
            val headers = arrayOf(
                ScheduleDbHelper.COLUMN_NAME,
                ScheduleDbHelper.COLUMN_PHONE,
                ScheduleDbHelper.COLUMN_DATE,
                ScheduleDbHelper.COLUMN_START_TIME
            )
            val headersInt = intArrayOf(R.id.database_name, R.id.database_phone, R.id.date, R.id.time)

            userAdapter = SimpleCursorAdapter(
                context, R.layout.search_list_item,
                userCursor, headers, headersInt, 0
            )

            // если в текстовом поле есть текст, выполняем фильтрацию
            userAdapter!!.filter.filter(userSearch.text.toString())

            // установка слушателя изменения текста
            userSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // при изменении текста выполняем фильтрацию
                    userAdapter!!.filter.filter(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            // установка слушателя измен ения текста
            userSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // при изменении текста выполняем фильтрацию
                    userAdapter!!.filter.filter(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            // устанавливаем провайдер фильтрации
            userAdapter!!.filterQueryProvider = FilterQueryProvider { p0 ->
                if (p0 == null || p0.isEmpty()) {
                    db!!.rawQuery(
                        "SELECT * FROM ${ScheduleDbHelper.TABLE_NAME} ORDER BY ${ScheduleDbHelper.COLUMN_DATE} DESC",
                        null
                    )
                } else {
                    val arr = arrayOf("%$p0%")
                    if (toggleButton?.isChecked == true) {
                        // Поиск по имени
                        db!!.rawQuery(
                            "SELECT * FROM ${ScheduleDbHelper.TABLE_NAME} WHERE ${ScheduleDbHelper.COLUMN_NAME} LIKE ? ORDER BY ${ScheduleDbHelper.COLUMN_DATE} DESC",
                            arr
                        )
                    } else {
                        // Поиск по номеру телефона
                        db!!.rawQuery(
                            "SELECT * FROM ${ScheduleDbHelper.TABLE_NAME} WHERE ${ScheduleDbHelper.COLUMN_PHONE} LIKE ? ORDER BY ${ScheduleDbHelper.COLUMN_DATE} DESC",
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
        toggleButton = binding.toggleSearchButton
        userSearch = binding.userSearch
        userSearch.inputType = InputType.TYPE_CLASS_PHONE
        userSearch.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        userCursor?.close()
        db?.close()
    }
}