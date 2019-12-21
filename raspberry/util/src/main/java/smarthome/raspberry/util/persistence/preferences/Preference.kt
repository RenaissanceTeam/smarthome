package smarthome.raspberry.util.persistence.preferences

import kotlin.reflect.KClass

interface Preference<T: Any> {
    val ofType: KClass<T>
    val key: String
    var default: T?
    fun get(): T
    suspend fun set(value: T)
}