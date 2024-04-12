package com.example.projectnailsschedule.presentation.clients

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ClientModelDb

class ClientsRvAdapter(
    private var clientsList: List<ClientModelDb>,
    private var clientsViewModel: ClientsViewModel
) : RecyclerView.Adapter<ClientsRvAdapter.ViewHolder>() {
    inner class ViewHolder(
        itemView: View,
        listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var phone: TextView
        var vk: TextView
        var telegram: TextView
        var instagram: TextView
        var whatsapp: TextView
        var notes: TextView
        var clientPhoto: ImageView

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
                clientPhoto = findViewById(R.id.client_avatar)

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

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.client_select_rv_item, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return clientsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        clearViews(holder)
        setTextViews(holder)
        setClickListeners(holder)
    }

    private fun setTextViews(holder: ViewHolder) {
        with(clientsList[holder.adapterPosition]) {
            holder.name.text = name
            holder.phone.text = phone
            holder.vk.text = vk
            holder.telegram.text = telegram
            holder.instagram.text = instagram
            holder.whatsapp.text = whatsapp
            holder.notes.text = notes
            // if client photo exists - set photo
            if (!photo.isNullOrEmpty()) {
                holder.clientPhoto.setImageURI(photo.toUri())
            } else {
                holder.clientPhoto.setImageResource(R.drawable.client_avatar)
            }
        }

        hideEmptyViews(holder, holder.adapterPosition)
    }

    private fun setClickListeners(holder: ViewHolder) {
        with(holder) {
            callClientButton.setOnClickListener {
                startPhone(holder.phone.text.toString())
            }

            vkImageButton.setOnClickListener {
                startVk(holder.vk.text.toString().trim())
            }

            telegramImageButton.setOnClickListener {
                startTelegram(holder.telegram.text.toString().trim())
            }

            instagramImageButton.setOnClickListener {
                startInstagram(holder.instagram.text.toString().trim())
            }

            whatsappImageButton.setOnClickListener {
                startWhatsapp(holder.whatsapp.text.toString().trim())
            }
        }
    }

    private fun startVk(uri: String) {
        clientsViewModel.startVk(uri)
    }

    private fun startTelegram(uri: String) {
        clientsViewModel.startTelegram(uri)
    }

    private fun startInstagram(uri: String) {
        clientsViewModel.startInstagram(uri)
    }

    private fun startWhatsapp(uri: String) {
        clientsViewModel.startWhatsApp(uri)
    }

    private fun startPhone(phoneNum: String) {
        clientsViewModel.startPhone(phoneNum)
    }

    private fun hideEmptyViews(holder: ViewHolder, position: Int) {
        with(clientsList[position]) {
            if (name.isNullOrEmpty()) {
                holder.clientNameCl.visibility = View.GONE
            }
            if (phone.isNullOrEmpty()) {
                holder.clientPhoneCl.visibility = View.GONE
            }
            if (vk.isNullOrEmpty()) {
                holder.clientVkCl.visibility = View.GONE
            }
            if (telegram.isNullOrEmpty()) {
                holder.clientTelegramCl.visibility = View.GONE
            }
            if (instagram.isNullOrEmpty()) {
                holder.clientInstagramCl.visibility = View.GONE
            }
            if (whatsapp.isNullOrEmpty()) {
                holder.clientWhatsappCl.visibility = View.GONE
            }
            if (notes.isNullOrEmpty()) {
                holder.clientNotesCl.visibility = View.GONE
            }
        }
    }

    private fun clearViews(holder: ViewHolder) {
        with(holder) {
            clientNameCl.visibility = View.VISIBLE
            clientPhoneCl.visibility = View.VISIBLE
            clientVkCl.visibility = View.VISIBLE
            clientTelegramCl.visibility = View.VISIBLE
            clientInstagramCl.visibility = View.VISIBLE
            clientWhatsappCl.visibility = View.VISIBLE
            clientNotesCl.visibility = View.VISIBLE
        }
    }
}