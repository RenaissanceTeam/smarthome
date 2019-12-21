package smarthome.raspberry.util.persistence

import io.reactivex.Observable
import smarthome.raspberry.util.persistence.preferences.ObservablePreference
import smarthome.raspberry.util.persistence.preferences.PersistentPreference
import smarthome.raspberry.util.persistence.preferences.Preference
import kotlin.reflect.KClass

class StorageHelper(private val storage: PersistentStorage) {
    private val preferences = mutableMapOf<String, Preference<*>>()
    
    suspend fun <T : Any> set(key: String, value: T, expectedType: KClass<T>) {
        obtainPreference(key, expectedType)
            .set(value)
    }
    
    fun <T: Any> setDefault(key: String, value: T, expectedType: KClass<T>) {
        obtainPreference(key, expectedType).default = value
    }
    
    private fun <T : Any> obtainPreference(key: String, expectedType: KClass<T>): Preference<T> {
        return when (preferences.contains(key)) {
            true -> {
                val pref = preferences[key]!!
                if (pref.ofType != expectedType) throw IllegalArgumentException()
                
                return withCast(pref)
            }
            false -> PersistentPreference(key, expectedType, storage).apply { preferences[key] = this }
        }
    }
    
    private inline fun <reified CONTEXT> withCast(data: Any) =
        data as? CONTEXT ?: throw IllegalStateException()
    
    
    fun <T : Any> get(key: String, expectedType: KClass<T>): T {
        val preference = obtainPreference(key, expectedType)
        val saved = preference.get()
        
        return convertToExpectedType(expectedType, saved)
    }
    
    private fun <T : Any> convertToExpectedType(expectedType: KClass<T>, from: Any): T {
        try {
            return expectedType.java.cast(from) ?: throw TypeAccessException(from::class, expectedType)
        } catch (e: ClassCastException) {
            throw IllegalArgumentException()
        }
    }

    fun <T: Any> observe(key: String, expectedType: KClass<T>): Observable<T> {
        val preference = obtainPreference(key, expectedType)
        val observable = obtainObservablePreference(preference, key)
    
        return observable.observe()
    }
    
    private fun <T : Any> obtainObservablePreference(
        preference: Preference<T>,
        key: String
    ): ObservablePreference<T> {
        val savedObservable = preference as? ObservablePreference<T>
        
        return savedObservable ?: ObservablePreference(preference).apply {
            preferences[key] = this
        }
    }
}