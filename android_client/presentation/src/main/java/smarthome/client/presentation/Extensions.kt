package smarthome.client.presentation

import android.view.View

var View.visible
    get() = visibility == View.VISIBLE
    set(value: Boolean) {
    visibility = when (value) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

fun <T> List<T>.replace(newItem: T, predicate: (T) -> Boolean): List<T> {
    val toRemove = find(predicate)
    val index = indexOf(toRemove)
    return subList(0, index) + listOf(newItem) + subList(index + 1, size)
}