package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class ClientsViewHolder internal constructor(
    itemView: View,
    listener: ClientsAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    var name: TextView
    var phone: TextView
    var vk: TextView
    var telegram: TextView
    var instagram: TextView
    var whatsapp: TextView
    var notes: TextView

    var callClientButton: ImageButton
    var vkImageButton: ImageButton
    var telegramImageButton: ImageButton
    var instagramImageButton: ImageButton

    init {
        name = itemView.findViewById(R.id.client_name_search)
        phone = itemView.findViewById(R.id.client_phone_search)
        vk = itemView.findViewById(R.id.client_vk_link_tw)
        telegram = itemView.findViewById(R.id.client_telegram_link_tv)
        instagram = itemView.findViewById(R.id.client_instagram_link_tv)
        whatsapp = itemView.findViewById(R.id.client_whatsapp_link_tv)
        notes = itemView.findViewById(R.id.client_notes_search)


        callClientButton = itemView.findViewById(R.id.call_client_button)
        vkImageButton = itemView.findViewById(R.id.vk_logo_imageButton)
        telegramImageButton = itemView.findViewById(R.id.telegram_logo_imageButton)
        instagramImageButton = itemView.findViewById(R.id.instagram_logo_imageButton)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}