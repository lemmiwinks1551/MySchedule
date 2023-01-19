package com.example.projectnailsschedule.presentation.calendar.dataShort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

internal class DateShortAdapter(
    private val rowsCount: Int,
    private val dateShorGetDbData: DateShortGetDbData,
    private val date: String
) :
    RecyclerView.Adapter<DateShortViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateShortViewHolder {
        // Get ViewHolder

        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.database_short_view, parent, false)
        return DateShortViewHolder(view, date)
    }

    override fun onBindViewHolder(holder: DateShortViewHolder, position: Int) {
        // Set name and date in ViewHolder

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