package smarthome.client.util

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

fun <T> List<T>.findAndModify(predicate: (T) -> Boolean, modify: (T) -> T): List<T> {
    val found = find(predicate) ?: return this
    val modified = modify(found)
    
    return replace(modified, predicate)
}

fun <T> List<T>.withRemoved(predicate: (T) -> Boolean): List<T> {
    val toRemove = find(predicate)
    val index = indexOf(toRemove)
    
    return subList(0, index) + subList(index + 1, size)
}

fun <T> List<T>.withReplacedOrAdded(item: T, predicate: (T) -> Boolean): List<T> {
    return if (containsThat(predicate)) {
        replace(item, predicate)
    } else {
        this + item
    }
}

fun <T> List<T>.containsThat(predicate: (T) -> Boolean): Boolean {
    return find(predicate) != null
}

inline fun <T, R> T.runInScope(scope: CoroutineScope, crossinline block: suspend T.() -> R): Job {
    return scope.launch { block() }
}

inline fun <T, R> T.runInScopeCatchingAny(scope: CoroutineScope,
                                          crossinline onFailure: (Throwable) -> Unit = {},
                                          crossinline block: suspend T.() -> R
                                          ): Job {
    return scope.launch { runCatching { block() }.onFailure(onFailure) }
}

