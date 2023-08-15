package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.SearchFragment
import org.w3c.dom.Text

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

    var vkLink: TextView
    var telegramLink: TextView
    var instagramLink: TextView
    var whatsAppLink: TextView

    var callClientButton: ImageButton
    var vkImageButton: ImageButton
    var telegramImageButton: ImageButton
    var instagramImageButton: ImageButton
    var whatsappImageButton: ImageButton

    init {
        name = itemView.findViewById(R.id.client_value_search)
        date = itemView.findViewById(R.id.date_value_search)
        phone = itemView.findViewById(R.id.phone_value_search)
        time = itemView.findViewById(R.id.time_value_search)
        notes = itemView.findViewById(R.id.client_notes)
        procedure = itemView.findViewById(R.id.procedure_value_search)

        vkLink = itemView.findViewById(R.id.client_vk_link_tv)
        telegramLink = itemView.findViewById(R.id.client_telegram_link_tv)
        instagramLink = itemView.findViewById(R.id.client_instagram_link_tv)
        whatsAppLink = itemView.findViewById(R.id.client_whatsapp_link_tv)

        callClientButton = itemView.findViewById(R.id.call_client_button)
        vkImageButton = itemView.findViewById(R.id.vk_logo_imageButton_date)
        telegramImageButton = itemView.findViewById(R.id.telegram_logo_imageButton_date)
        instagramImageButton = itemView.findViewById(R.id.instagram_logo_imageButton_date)
        whatsappImageButton = itemView.findViewById(R.id.whatsapp_logo_imageButton_date)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}