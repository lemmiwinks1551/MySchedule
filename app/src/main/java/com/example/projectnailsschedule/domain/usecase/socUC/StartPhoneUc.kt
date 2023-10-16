package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.projectnailsschedule.R

class StartPhoneUc(val context: Context) {

    fun execute(phoneNum: String) {
        if (phoneNum.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum"))
            context.startActivity(intent)
        } else {
            Toast.makeText(context, R.string.no_data_error, Toast.LENGTH_LONG).show()
        }
    }
}