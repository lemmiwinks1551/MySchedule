package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.projectnailsschedule.R

class StartInstagramUc(val context: Context) {

    fun execute(uri: String) {
        if (uri.isNotEmpty()) {
            val regex = Regex("https:\\/\\/instagram\\.com\\/([^?\\/]+)(?:\\?igshid=.*)?|([\\w.-]+)")
            val matchResult = regex.find(uri)
            var username = matchResult?.groupValues?.getOrNull(1)
            if (username == "") {
                username = matchResult?.groupValues?.getOrNull(2)
            }
            if (username == "https") {
                val filter0 = uri.replace("https://www.instagram.com/", "")
                val filter1 = filter0.replace(Regex("\\?igsh.+"), "")
                val filter2 = filter1.replace(Regex("/.+"), "")
                val filter3 = filter2.replace("/", "")
                username = filter3
            }

            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$username"))
                intent.setPackage("com.instagram.android")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                try {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$username"))
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