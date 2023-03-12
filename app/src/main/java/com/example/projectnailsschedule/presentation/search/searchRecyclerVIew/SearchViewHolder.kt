package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.search.SearchFragment

class SearchViewHolder internal constructor(itemView: View, onItemListener: SearchFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val onItemListener: SearchAdapter.OnItemListener
    var name: TextView
    var date: TextView
    var phone: TextView
    var time: TextView
    var misc: TextView

    init {
        name = itemView.findViewById(R.id.client_name)
        date = itemView.findViewById(R.id.appointment_date)
        phone = itemView.findViewById(R.id.client_phone)
        time = itemView.findViewById(R.id.time_value)
        misc = itemView.findViewById(R.id.misc_text)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        onItemListener.onItemClick(adapterPosition, date.text as String)
    }
}