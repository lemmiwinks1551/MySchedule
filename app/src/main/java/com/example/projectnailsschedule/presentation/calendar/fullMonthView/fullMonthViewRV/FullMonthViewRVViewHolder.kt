package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class FullMonthViewRVViewHolder internal constructor(
    itemView: View,
    listener: FullMonthViewRVAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {

    val appointmentDate: TextView
    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentClientPhone: TextView
    val appointmentNotes: TextView

    var callClientButton: ImageButton

    init {
        appointmentDate = itemView.findViewById(R.id.date_value_search)
        appointmentTime = itemView.findViewById(R.id.time_value_search)
        appointmentProcedure = itemView.findViewById(R.id.procedure_value_search)
        appointmentClientName = itemView.findViewById(R.id.client_value_search)
        appointmentClientPhone = itemView.findViewById(R.id.phone_value_search)
        appointmentNotes = itemView.findViewById(R.id.misc_value_search)

        callClientButton = itemView.findViewById(R.id.call_client_button)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}