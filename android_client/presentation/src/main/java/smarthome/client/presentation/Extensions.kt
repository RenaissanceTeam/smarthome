package smarthome.client.presentation

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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

inline fun <T, R> T.runInScope(scope: CoroutineScope, crossinline block: suspend T.() -> R): Job {
    return scope.launch { block() }
}