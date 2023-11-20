package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.projectnailsschedule.R

class StartVkUc(val context: Context) {

    fun execute(uri: String) {
        if (uri.isNotEmpty()) {
            val uri = uri.replace("https://vk.com/", "")
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
                    browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
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