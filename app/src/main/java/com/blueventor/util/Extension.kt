package com.blueventor.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.blueventor.BuildConfig

fun logDebugMessage(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
       println("${tag}  ${message}")
    }
}

inline fun <reified T : Activity> Context.startNewActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun Context.showAlert(message: String) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}


