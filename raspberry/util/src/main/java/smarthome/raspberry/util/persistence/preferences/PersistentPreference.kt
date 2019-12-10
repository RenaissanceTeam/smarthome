package smarthome.raspberry.util.persistence.preferences

import smarthome.raspberry.util.persistence.PersistentStorage
import kotlin.reflect.KClass

class PersistentPreference<T : Any>(val key: String,
                                    override val ofType: KClass<T>,
                                    val persistentStorage: PersistentStorage): Preference<T> {
    override fun get() = persistentStorage.get(key, ofType)
    override suspend fun set(value: T) = persistentStorage.set(key, value)
}

