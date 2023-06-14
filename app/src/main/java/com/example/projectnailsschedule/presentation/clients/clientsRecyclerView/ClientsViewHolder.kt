package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.clients.ClientsFragment

class ClientsViewHolder internal constructor(itemView: View, onItemListener: ClientsFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var name: TextView
    var phone: TextView
    var notes: TextView

    init {
        name = itemView.findViewById(R.id.client_name_search)
        phone = itemView.findViewById(R.id.client_phone_search)
        notes = itemView.findViewById(R.id.client_notes_search)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}