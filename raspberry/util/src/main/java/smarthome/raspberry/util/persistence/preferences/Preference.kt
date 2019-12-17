package smarthome.raspberry.util.persistence.preferences

import kotlin.reflect.KClass

interface Preference<T: Any> {
    val ofType: KClass<T>
    fun get(): T
    suspend fun set(value: T)
}