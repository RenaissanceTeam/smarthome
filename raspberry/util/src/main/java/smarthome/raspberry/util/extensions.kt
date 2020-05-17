package smarthome.raspberry.util

import java.util.*

infix fun <K, V> Map<K, V>.has(key: K) = this[key] != null

fun <T, R> Optional<T>.fold(onNone: () -> R, onSome: (T) -> R): R {
    if (isPresent) return onSome(get())
    return onNone()
}