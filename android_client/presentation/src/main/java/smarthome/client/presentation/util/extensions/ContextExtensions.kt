package smarthome.client.presentation.util.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(value: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, value, duration).show()

fun Context.showNotImplementedToast(message: String = "") = showToast("NOT IMPLEMENTED. $message")