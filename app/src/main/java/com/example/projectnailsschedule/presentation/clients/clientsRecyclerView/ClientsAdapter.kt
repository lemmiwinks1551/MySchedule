package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ClientModelDb

class ClientsAdapter(
    private var clientsCount: Int,
    private var clientsList: List<ClientModelDb>,
    private var context: Context
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
        // set current clients count
        return clientsCount
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int) {
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
    }

    private fun setClickListeners(holder: ClientsViewHolder) {
        with(holder) {
            callClientButton.setOnClickListener {
                val phone = clientsList[adapterPosition].phone.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(intent)
            }
        }
    }
}