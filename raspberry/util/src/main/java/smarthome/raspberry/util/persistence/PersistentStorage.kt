package smarthome.raspberry.util.persistence

import kotlin.reflect.KClass

interface PersistentStorage {
    suspend fun <T: Any> set(key: String, value: T)
    fun <T: Any> get(key: String, typeParam: KClass<T>): T
}
