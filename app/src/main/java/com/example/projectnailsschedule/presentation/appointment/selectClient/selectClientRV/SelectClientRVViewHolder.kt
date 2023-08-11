package com.example.projectnailsschedule.presentation.appointment.selectClient.selectClientRV

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsAdapter

class SelectClientRVViewHolder internal constructor(
    itemView: View,
    listener: SelectClientRVAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {

    lateinit var name: TextView
    lateinit var phone: TextView
    lateinit var vk: TextView
    lateinit var telegram: TextView
    lateinit var instagram: TextView
    lateinit var whatsapp: TextView
    lateinit var notes: TextView

    lateinit var callClientButton: ImageButton
    lateinit var vkLogoImageButton: ImageButton
    lateinit var telegramLogoImageButton: ImageButton
    lateinit var instagramLogoImageButton: ImageButton
    lateinit var whatsappLogoImageButton: ImageButton

    init {
        inflateViews()
        initButtons()
        setClickListeners(listener)
    }

    private fun inflateViews() {
        with(itemView) {
            name = findViewById(R.id.client_select_name)
            phone = findViewById(R.id.client_select_phone)
            vk = findViewById(R.id.client_select_vk_link_tv)
            telegram = findViewById(R.id.client_select_telegram_link_tv)
            instagram = findViewById(R.id.client_select_instagram_link_tv)
            whatsapp = findViewById(R.id.client_select_whatsapp_link_tv)
            notes = findViewById(R.id.client_select_notes)
        }
    }

    private fun initButtons() {
        with(itemView) {
            callClientButton = findViewById(R.id.call_client_button_select_button)
            vkLogoImageButton = findViewById(R.id.vk_logo_imageButton_select_client)
            telegramLogoImageButton = findViewById(R.id.telegram_logo_imageButton_select_client)
            instagramLogoImageButton = findViewById(R.id.instagram_logo_imageButton_select_client)
            whatsappLogoImageButton = findViewById(R.id.whatsapp_logo_imageButton_select_client)
        }
    }

    private fun setClickListeners(listener: SelectClientRVAdapter.OnItemClickListener) {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}