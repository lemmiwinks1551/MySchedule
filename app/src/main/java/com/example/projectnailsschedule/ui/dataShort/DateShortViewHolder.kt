package com.example.projectnailsschedule.ui.dataShort

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.DateActivity
import com.example.projectnailsschedule.R

class DateShortViewHolder internal constructor(itemView: View, _date: String) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val clientName: TextView
    val starTime: TextView
    private var date = ""

    init {
        clientName = itemView.findViewById(R.id.appointment_name_short)
        starTime = itemView.findViewById(R.id.appointment_start_short)
        date = _date
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(itemView.context, DateActivity::class.java)
        intent.putExtra("day", date)
        itemView.context.startActivity(intent)
    }
}