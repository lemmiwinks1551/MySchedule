package com.example.projectnailsschedule.domain.usecase.socUC

import android.content.Context
import android.content.Intent
import android.net.Uri

class StartPhoneUc(val context: Context) {

    fun execute(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum"))
        context.startActivity(intent)
    }
}