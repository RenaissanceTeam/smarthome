package smarthome.client.data.util

import io.reactivex.observers.BaseTestConsumer

fun <T, U : BaseTestConsumer<T, U>> BaseTestConsumer<T, U>.assertThrough(
    predicate: (T) -> Boolean): U {
    if (values().find(predicate) == null)
        throw AssertionError("No value that match predicate, all items are: ${values()}")
    return this as U
}

fun <T, U : BaseTestConsumer<T, U>> BaseTestConsumer<T, U>.assertThroughSequence(
    vararg predicates: (T) -> Boolean): U {
    var currentMatch = 0
    for (value in values()) {
        if (predicates[currentMatch](value)) {
            currentMatch += 1
        } else {
            currentMatch = 0
        }
        
        if (currentMatch == predicates.size) return this as U
    }
    
    throw AssertionError("No sequence found, all items are: ${values()}")
}

