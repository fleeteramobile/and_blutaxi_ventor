package com.blueventor.util

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.blueventor.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

fun View.hideView() {
    this.visibility = View.GONE

}

fun View.showView() {
    this.visibility = View.VISIBLE
}
fun setOnclick(button :Button, action: () -> Unit )
{
    button.setOnClickListener {
        action()
    }

}

fun setImageOnclick(imageview: ImageView,action: () -> Unit)
{
    imageview.setOnClickListener {
        action()
    }
}

inline fun View.onclik(crossinline action: () -> Unit)
{
    this.setOnClickListener {
        action()
    }
}

inline fun setOnclicks(button : Button,crossinline  action: () -> Unit)
{
    button.setOnClickListener {
        action()
    }

}

fun ImageView.loadImage(
    url: String?,
    placeholder: Int? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    if (url.isNullOrEmpty()) {
        placeholder?.let { this.setImageResource(it) }
        return
    }

    Glide.with(this.context)
        .load(url)
        .apply {
            placeholder?.let { placeholder(it) }
            error(placeholder ?: 0)
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
            override fun onResourceReady(
                resource: android.graphics.drawable.Drawable,
                transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?
            ) {
                this@loadImage.setImageDrawable(resource)
                onSuccess?.invoke()
            }

            override fun onLoadCleared(placeholderDrawable: android.graphics.drawable.Drawable?) {
                placeholder?.let { this@loadImage.setImageResource(it) }
            }

            override fun onLoadFailed(errorDrawable: android.graphics.drawable.Drawable?) {
                super.onLoadFailed(errorDrawable)
                onError?.invoke()
            }
        })
}




