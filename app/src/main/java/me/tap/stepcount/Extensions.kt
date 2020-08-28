package me.tap.stepcount

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}