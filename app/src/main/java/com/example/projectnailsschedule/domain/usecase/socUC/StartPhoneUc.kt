package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.projectnailsschedule.R

class StartPhoneUc(val context: Context) {

    fun execute(phoneNum: String) {
        try {
            if (phoneNum.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } else {
                Toast.makeText(context, R.string.no_data_error, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.unknown_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}