package smarthome.client.presentation.util.extensions

import android.widget.TextView
import smarthome.client.presentation.R
import kotlin.math.abs

fun Pair<Int, Int>.abs() = Pair(abs(first), abs(second))


fun TextView.setTextOrEmptyPlaceholder(value: String?, placeholder: String = "Empty") {
    if (value.isNullOrEmpty()) {
        text = placeholder
        setTextColor(context.getColorResCompat(R.attr.textColorEmpty))
    } else {
        text = value
        setTextColor(context.getColorResCompat(android.R.attr.textColorPrimary))
    }
}