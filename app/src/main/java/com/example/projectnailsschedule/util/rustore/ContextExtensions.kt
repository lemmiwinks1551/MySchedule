package com.example.projectnailsschedule.util.rustore

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.projectnailsschedule.R

/** Extension-функция к Context для вывода диалогового окна */

fun Context.showAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit = {},
) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(context.getString(R.string.billing_common_ok)) { dialog, _ ->
            dialog.dismiss()
        }
        setOnDismissListener { onDismiss.invoke() }
        show()
    }
}
