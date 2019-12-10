package smarthome.raspberry.util.persistence.preferences

import smarthome.raspberry.util.persistence.PersistentStorage
import kotlin.reflect.KClass

class Preference<T : Any>(val key: String,
                                  val ofType: KClass<T>,
                                  val persistentStorage: PersistentStorage) {
    fun get() = persistentStorage.get(key, ofType)
    suspend fun set(value: T) = persistentStorage.set(key, value)
}