package com.blueventor.util

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.blueventor.BuildConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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


fun showDatePicker(editText: TextView) {
    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(
        editText.context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            editText.setText(dateFormat.format(selectedDate.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // ✅ Set maximum selectable date = today
    datePicker.datePicker.maxDate = System.currentTimeMillis()

    datePicker.show()

}



