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
): RecyclerView.ViewHolder(itemView) {

    var name: TextView
    var phone: TextView
    var notes: TextView
    var callClientButton: ImageButton

    init {
        name = itemView.findViewById(R.id.client_name_search)
        phone = itemView.findViewById(R.id.client_phone_search)
        notes = itemView.findViewById(R.id.client_notes_search)
        callClientButton = itemView.findViewById(R.id.call_client_button)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}