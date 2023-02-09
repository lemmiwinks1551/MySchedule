package com.example.projectnailsschedule.presentation.calendar.dataShort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.data.storage.DateShortDbHelper
import com.example.projectnailsschedule.domain.models.DateParams
import java.time.LocalDate

internal class DateShortAdapter(
    private val rowsCount: Int,
    private val dateShorGetDbData: DateShortDbHelper,
    private val selectedDate: LocalDate
) :
    RecyclerView.Adapter<DateShortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateShortViewHolder {
        // Set ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.database_short_view, parent, false)
        return DateShortViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateShortViewHolder, position: Int) {
        // Set name and date in ViewHolder
        val dateParams = DateParams(
            _id = null,
            date = selectedDate,
            status = null)

        val map = dateShorGetDbData.getTimeNameMap()            // Create map name:time
        val namesArray:Array<String> = map.keys.toTypedArray()  // Create array of keys (names)

        if (map.size > position) {
            holder.clientName.text = namesArray[position]       // Set name in holder
            holder.starTime.text = map[namesArray[position]]    // Set time in holder
        }
    }

    override fun getItemCount(): Int {
        return rowsCount
    }
}