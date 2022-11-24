package com.example.projectnailsschedule.ui.settings

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.database.SettingsDbHelper
import com.example.projectnailsschedule.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var settingsDbHelper: SettingsDbHelper? = null
    private var db: SQLiteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val clientsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsDbHelper = SettingsDbHelper(context)
        db = settingsDbHelper?.writableDatabase

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Set listener om theme switch
        binding.darkThemeSwitch.setOnClickListener {
            if (binding.darkThemeSwitch.isChecked) {
                settingsDbHelper!!.updateRow("theme", "dark", db!!)
                setTheme("dark")
            } else {
                settingsDbHelper!!.updateRow("theme", "light", db!!)
                setTheme("light")
            }
        }

        return binding.root
    }

    fun setTheme(theme: String) {
        // TODO: Положение выключателя можно изменять для красоты
        if (theme == "dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db!!.close()
        _binding = null
    }
}