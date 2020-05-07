package smarthome.client.presentation.util.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

fun Context.showToast(value: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, value, duration).show()

fun Context.showNotImplementedToast(message: String = "") = showToast("NOT IMPLEMENTED. $message")

@ColorInt
@SuppressLint("ResourceAsColor")
fun Context.getColorResCompat(@AttrRes id: Int): Int {
    val resolvedAttr = TypedValue()
    theme.resolveAttribute(id, resolvedAttr, true)
    val colorRes = resolvedAttr.run { if (resourceId != 0) resourceId else data }
    return ContextCompat.getColor(this, colorRes)
}