package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel

class ClientsAdapter(
    private var clientsCount: Int,
    private var clientsList: List<ClientModelDb>,
    private var clientsViewModel: ClientsViewModel
) : RecyclerView.Adapter<ClientsViewHolder>(
) {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.client_select_rv_item, parent, false)

        return ClientsViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return clientsCount
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int) {
        clearViews(holder)
        setTextViews(holder)
        setClickListeners(holder)
    }

    private fun setTextViews(holder: ClientsViewHolder) {
        with(clientsList[holder.adapterPosition]) {
            holder.name.text = name
            holder.phone.text = phone
            holder.vk.text = vk
            holder.telegram.text = telegram
            holder.instagram.text = instagram
            holder.whatsapp.text = whatsapp
            holder.notes.text = notes
        }

        hideEmptyViews(holder, holder.adapterPosition)
    }

    private fun setClickListeners(holder: ClientsViewHolder) {
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

    private fun hideEmptyViews(holder: ClientsViewHolder, position: Int) {
        if (clientsList[position].name.isNullOrEmpty()) {
            holder.clientNameCl.visibility = View.GONE
        }
        if (clientsList[position].phone.isNullOrEmpty()) {
            holder.clientPhoneCl.visibility = View.GONE
        }
        if (clientsList[position].vk.isNullOrEmpty()) {
            holder.clientVkCl.visibility = View.GONE
        }
        if (clientsList[position].telegram.isNullOrEmpty()) {
            holder.clientTelegramCl.visibility = View.GONE
        }
        if (clientsList[position].instagram.isNullOrEmpty()) {
            holder.clientInstagramCl.visibility = View.GONE
        }
        if (clientsList[position].whatsapp.isNullOrEmpty()) {
            holder.clientWhatsappCl.visibility = View.GONE
        }
        if (clientsList[position].notes.isNullOrEmpty()) {
            holder.clientNotesCl.visibility = View.GONE
        }
    }

    private fun clearViews(holder: ClientsViewHolder) {
        holder.clientNameCl.visibility = View.VISIBLE
        holder.clientPhoneCl.visibility = View.VISIBLE
        holder.clientVkCl.visibility = View.VISIBLE
        holder.clientTelegramCl.visibility = View.VISIBLE
        holder.clientInstagramCl.visibility = View.VISIBLE
        holder.clientWhatsappCl.visibility = View.VISIBLE
        holder.clientNotesCl.visibility = View.VISIBLE
    }
}