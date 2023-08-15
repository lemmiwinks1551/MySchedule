package com.example.projectnailsschedule.presentation.appointment.selectClient.selectClientRV

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsViewHolder

class SelectClientRVAdapter(
    private var clientsCount: Int,
    private var clientsList: List<ClientModelDb>,
    private var context: Context
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
    }

    private fun setClickListeners(holder: SelectClientRVViewHolder, position: Int) {
        with(clientsList[position]) {
            holder.callClientButton.setOnClickListener {
                val phone = phone
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(intent)
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
        val uri = uri.replace("https://vk.com/", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Не удалось перейти во Вконтакте",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startTelegram(uri: String) {
        val uri = uri.replace("https://t.me/", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$uri"))
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$uri"))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Не удалось перейти в Telegram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startInstagram(uri: String) {
        val regex = Regex("https:\\/\\/instagram\\.com\\/([^?\\/]+)(?:\\?igshid=.*)?|([\\w.-]+)")
        val matchResult = regex.find(uri)
        var username = matchResult?.groupValues?.getOrNull(1)
        if (username == "") {
            username = matchResult?.groupValues?.getOrNull(2)
        }

        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$username"))
            intent.setPackage("com.instagram.android")
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$username"))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Не удалось перейти в Instagram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startWhatsapp(uri: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$uri"))
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://web.whatsapp.com/send?phone=$uri")
                )
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Не удалось перейти в Whatsapp",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}