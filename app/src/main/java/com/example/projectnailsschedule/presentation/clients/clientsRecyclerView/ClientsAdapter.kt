package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.ClientsFragment

class ClientsAdapter(
    private var clientsCount: Int,
    private val clientsFragment: ClientsFragment,
    private var clientsList: List<ClientModelDb>
) : RecyclerView.Adapter<ClientsViewHolder>(
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.client_recycler_view_item, parent, false)

        return ClientsViewHolder(view, clientsFragment)
    }

    override fun getItemCount(): Int {
        // set current appointment count
        return clientsCount
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int) {
        holder.name.text = clientsList[position].name.toString()
        holder.phone.text = clientsList[position].phone.toString()
        holder.notes.text = clientsList[position].notes.toString()
    }
}