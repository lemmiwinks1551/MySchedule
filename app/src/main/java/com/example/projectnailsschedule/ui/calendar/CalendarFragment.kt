package com.example.projectnailsschedule.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    //
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.e("LifeCycle", "CalendarFragment created")
        // Создаем переменную ViewModel
        val calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        calendarViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val calendar: CalendarView = binding.calendar
        calendar.date = 1
        return root
    }

    override fun onDestroyView() {
        Log.e("LifeCycle", "CalendarFragment onDestroy")
        super.onDestroyView()
        _binding = null
    }
}