package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
    var whatsappImageButton: ImageButton

    var clientNameCl: ConstraintLayout
    var clientPhoneCl: ConstraintLayout
    var clientVkCl: ConstraintLayout
    var clientTelegramCl: ConstraintLayout
    var clientInstagramCl: ConstraintLayout
    var clientWhatsappCl: ConstraintLayout
    var clientNotesCl: ConstraintLayout

    init {
        with(itemView) {
            name = findViewById(R.id.client_select_name)
            phone = findViewById(R.id.client_select_phone)
            vk = findViewById(R.id.client_select_vk_link_tv)
            telegram = findViewById(R.id.client_select_telegram_link_tv)
            instagram = findViewById(R.id.client_select_instagram_link_tv)
            whatsapp = findViewById(R.id.client_select_whatsapp_link_tv)
            notes = findViewById(R.id.client_select_notes)

            callClientButton = findViewById(R.id.call_client_button_select_button)
            vkImageButton = findViewById(R.id.vk_logo_imageButton_select_client)
            telegramImageButton = findViewById(R.id.telegram_logo_imageButton_select_client)
            instagramImageButton = findViewById(R.id.instagram_logo_imageButton_select_client)
            whatsappImageButton = findViewById(R.id.whatsapp_logo_imageButton_select_client)

            clientNameCl = findViewById(R.id.client_name_cl)
            clientPhoneCl = findViewById(R.id.client_phone_cl)
            clientVkCl = findViewById(R.id.client_vk_cl)
            clientTelegramCl = findViewById(R.id.client_telegram_cl)
            clientInstagramCl = findViewById(R.id.client_instagram_cl)
            clientWhatsappCl = findViewById(R.id.client_whatsapp_cl)
            clientNotesCl = findViewById(R.id.client_notes_cl)
        }

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}