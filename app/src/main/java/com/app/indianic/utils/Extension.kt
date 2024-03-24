package com.app.indianic.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.app.indianic.BuildConfig


fun Context.getContextColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun CardView.changeBackgroundColor(color: Int) {
    this.setCardBackgroundColor(this.context.getContextColor(color))
}

fun Context.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showShortToast(message: String?) {
    message?.let {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Any.logError(description: String) {
    if (BuildConfig.DEBUG) {
        Log.e(this::class.java.simpleName, description)
    }
}




