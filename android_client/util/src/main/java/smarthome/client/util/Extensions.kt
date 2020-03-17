package smarthome.client.util

import android.view.View
import io.reactivex.subjects.BehaviorSubject
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
    return replaceAt(indexOf(find(predicate)), newItem)
}

fun <T> List<T>.replaceAt(index: Int, newItem: T): List<T> {
    return subList(0, index) + listOf(newItem) + subList(index + 1, size)
}

fun <T> MutableList<T>.filterRemove(predicate: (T) -> Boolean) {
    val each = iterator()
    
    while (each.hasNext()) {
        if (predicate(each.next())) each.remove()
    }
}


fun <T> List<T>.findAndModify(predicate: (T) -> Boolean, modify: (T) -> T): List<T> {
    val found = find(predicate) ?: return this
    val modified = modify(found)
    
    return replace(modified, predicate)
}

fun <T> MutableList<T>.findAndReplace(predicate: (T) -> Boolean, replaceWith: (T) -> T) {
    val position = indexOf(find(predicate))
    val before = this[position]
    this[position] = replaceWith(before)
}

fun <T> List<T>.withRemoved(predicate: (T) -> Boolean): List<T> {
    val toRemove = find(predicate)
    val index = indexOf(toRemove).takeIf { it != -1 } ?: return this
    
    return subList(0, index) + subList(index + 1, size)
}

fun <T> List<T>.withReplacedOrAdded(item: T, predicate: (T) -> Boolean): List<T> {
    return if (containsThat(predicate)) {
        replace(item, predicate)
    } else {
        this + item
    }
}

fun <T> List<T>.withInserted(index: Int, item: T): List<T> {
    return subList(0, index) + item + subList(index, size)
}


fun <T> List<T>.containsThat(predicate: (T) -> Boolean): Boolean {
    return find(predicate) != null
}

fun <T> List<T>.containsAny(list: List<T>): Boolean {
    return intersect(list).isNotEmpty()
}

fun <T> List<T>.containsNone(list: List<T>): Boolean {
    return intersect(list).isEmpty()
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

fun <T> BehaviorSubject<List<T>>.onNextModified(modify: (List<T>) -> List<T>) {
    onNext(modify(value.orEmpty()))
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}



