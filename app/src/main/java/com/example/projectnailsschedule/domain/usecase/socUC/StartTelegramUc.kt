package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.projectnailsschedule.R

class StartTelegramUc(val context: Context) {

    fun execute(uri: String) {
        if (uri.isNotEmpty()) {
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
                        context.getString(R.string.unknown_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, R.string.no_data_error, Toast.LENGTH_LONG).show()
        }
    }
}