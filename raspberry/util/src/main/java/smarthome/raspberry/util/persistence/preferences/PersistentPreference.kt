package smarthome.raspberry.util.persistence.preferences

import smarthome.raspberry.util.persistence.NoStoredPreference
import smarthome.raspberry.util.persistence.PersistentStorage
import kotlin.reflect.KClass

class PersistentPreference<T : Any>(override val key: String,
                                    override val ofType: KClass<T>,
                                    private val persistentStorage: PersistentStorage): Preference<T> {
    override var default: T? = null
    
    override fun get(): T {
        return try {
            persistentStorage.get(key, ofType)
        } catch (e: NoStoredPreference) {
            default ?: throw e
        }
    }
    
    override suspend fun set(value: T) = persistentStorage.set(key, value)
}
