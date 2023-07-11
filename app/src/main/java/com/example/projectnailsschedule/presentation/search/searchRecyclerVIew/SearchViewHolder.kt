package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.SearchFragment

class SearchViewHolder internal constructor(
    itemView: View,
    listener: SearchRvAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    var appointmentId: Int? = null
    var appointmentDate: String? = null
    var appointmentModelDb: AppointmentModelDb? = null
    var position: Int? = null

    var name: TextView
    var date: TextView
    var phone: TextView
    var time: TextView
    var notes: TextView
    var procedure: TextView
    var callClientButton: ImageButton

    init {
        name = itemView.findViewById(R.id.client_value_search)
        date = itemView.findViewById(R.id.date_value_search)
        phone = itemView.findViewById(R.id.phone_value_search)
        time = itemView.findViewById(R.id.time_value_search)
        notes = itemView.findViewById(R.id.misc_value_search)
        procedure = itemView.findViewById(R.id.procedure_value_search)
        callClientButton = itemView.findViewById(R.id.call_client_button)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}