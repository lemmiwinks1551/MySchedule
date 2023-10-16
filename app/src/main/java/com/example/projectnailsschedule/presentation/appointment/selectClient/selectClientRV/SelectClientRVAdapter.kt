package com.example.projectnailsschedule.presentation.appointment.selectClient.selectClientRV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.appointment.selectClient.SelectClientViewModel

class SelectClientRVAdapter(
    private var clientsCount: Int,
    private var clientsList: List<ClientModelDb>,
    private var selectClientViewModel: SelectClientViewModel
) : RecyclerView.Adapter<SelectClientRVViewHolder>(
) {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectClientRVViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.client_select_rv_item, parent, false)

        return SelectClientRVViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        // set current appointment count
        return clientsCount
    }

    override fun onBindViewHolder(holder: SelectClientRVViewHolder, position: Int) {
        inflateViews(holder, position)
        setClickListeners(holder, position)
    }

    private fun inflateViews(holder: SelectClientRVViewHolder, position: Int) {
        with(clientsList[position]) {
            holder.name.text = name
            holder.phone.text = phone
            holder.vk.text = vk
            holder.telegram.text = telegram
            holder.instagram.text = instagram
            holder.whatsapp.text = whatsapp
            holder.notes.text = notes
        }

        hideEmptyViews(holder, position)
    }

    private fun setClickListeners(holder: SelectClientRVViewHolder, position: Int) {
        with(clientsList[position]) {
            holder.callClientButton.setOnClickListener {
                selectClientViewModel.startPhone(phone!!)
            }
        }

        initSocClickListeners(holder)
    }

    private fun initSocClickListeners(holder: SelectClientRVViewHolder) {
        with(holder) {
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
        selectClientViewModel.startVk(uri)
    }

    private fun startTelegram(uri: String) {
        selectClientViewModel.startTelegram(uri)
    }

    private fun startInstagram(uri: String) {
        selectClientViewModel.startInstagram(uri)
    }

    private fun startWhatsapp(uri: String) {
        selectClientViewModel.startWhatsApp(uri)
    }

    private fun hideEmptyViews(holder: SelectClientRVViewHolder, position: Int) {
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
}