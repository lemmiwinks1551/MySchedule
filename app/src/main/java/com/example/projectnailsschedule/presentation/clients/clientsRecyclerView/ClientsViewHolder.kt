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
        name = itemView.findViewById(R.id.client_select_name)
        phone = itemView.findViewById(R.id.client_select_phone)
        vk = itemView.findViewById(R.id.client_select_vk_link_tv)
        telegram = itemView.findViewById(R.id.client_select_telegram_link_tv)
        instagram = itemView.findViewById(R.id.client_select_instagram_link_tv)
        whatsapp = itemView.findViewById(R.id.client_select_whatsapp_link_tv)
        notes = itemView.findViewById(R.id.client_select_notes)


        callClientButton = itemView.findViewById(R.id.call_client_button_select_button)
        vkImageButton = itemView.findViewById(R.id.vk_logo_imageButton_select_client)
        telegramImageButton = itemView.findViewById(R.id.telegram_logo_imageButton_select_client)
        instagramImageButton = itemView.findViewById(R.id.instagram_logo_imageButton_select_client)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}