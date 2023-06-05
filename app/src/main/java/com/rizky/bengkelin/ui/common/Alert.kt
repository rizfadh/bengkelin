package com.rizky.bengkelin.ui.common

import android.app.AlertDialog
import android.content.Context

fun alert(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}