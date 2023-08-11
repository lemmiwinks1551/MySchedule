package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class DateViewHolder internal constructor(
    itemView: View,
    listener: DateAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    var appointmentDate: String? = null

    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentClientPhone: TextView
    val appointmentClientVk: TextView
    val appointmentClientTelegram: TextView
    val appointmentClientInstagram: TextView
    val appointmentClientWhatsapp: TextView
    val appointmentNotes: TextView

    var callClientButton: ImageButton

    init {
        appointmentTime = itemView.findViewById(R.id.time_value_search)
        appointmentProcedure = itemView.findViewById(R.id.procedure_value_search)
        appointmentClientName = itemView.findViewById(R.id.client_value_search)
        appointmentClientPhone = itemView.findViewById(R.id.phone_value_search)
        appointmentClientVk = itemView.findViewById(R.id.client_vk_link_tv)
        appointmentClientTelegram = itemView.findViewById(R.id.client_telegram_link_tv)
        appointmentClientInstagram = itemView.findViewById(R.id.client_instagram_link_tv)
        appointmentClientWhatsapp = itemView.findViewById(R.id.client_whatsapp_link_tv)
        appointmentNotes = itemView.findViewById(R.id.client_notes)

        callClientButton = itemView.findViewById(R.id.call_client_button)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}