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
        val view: View = inflater.inflate(R.layout.client_rv_item, parent, false)

        return ClientsViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        // set current clients count
        return clientsCount
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int) {
        holder.name.text = clientsList[position].name.toString()
        holder.phone.text = clientsList[position].phone.toString()
        holder.notes.text = clientsList[position].notes.toString()

        holder.callClientButton.setOnClickListener {
            val phone = clientsList[position].phone.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }
    }
}