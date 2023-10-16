package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.projectnailsschedule.R

class StartWhatsAppUc(val context: Context) {

    fun execute(uri: String) {
        if (uri.isNotEmpty()) {
            try {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=$uri")
                    )
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