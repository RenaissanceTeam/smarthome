package smarthome.raspberry.util.persistence

import io.reactivex.Observable
import smarthome.raspberry.util.persistence.preferences.ObservablePreference
import smarthome.raspberry.util.persistence.preferences.PersistentPreference
import smarthome.raspberry.util.persistence.preferences.Preference
import kotlin.reflect.KClass

class StorageHelper(private val storage: PersistentStorage) {
    private val preferences = mutableMapOf<String, Preference<*>>()
    
    private inline fun <reified CONTEXT> withCast(data: Any) =
        data as? CONTEXT ?: throw IllegalStateException()
    
    
    suspend fun <T : Any> set(key: String, value: T, expectedType: KClass<T>) {
        obtainPreference(key, expectedType).set(value)
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
    
    fun <T : Any> get(key: String, expectedType: KClass<T>): T {
        val preference = getPreferenceByKey(key)
        val saved = preference.get()
        
        return convertToExpectedType(expectedType, saved)
    }
    
    private fun <T : Any> convertToExpectedType(expectedType: KClass<T>, from: Any): T {
        try {
            return expectedType.java.cast(from) ?: throw IllegalStateException()
        } catch (e: ClassCastException) {
            throw IllegalArgumentException()
        }
    }
    
    private fun getPreferenceByKey(key: String): Preference<*> {
        return preferences[key] ?: throw IllegalArgumentException(
            "can't access key=$key as it is not stored")
    }
    
    fun <T: Any> observe(key: String, expectedType: KClass<T>): Observable<T> {
        val preference = obtainPreference(key, expectedType)
        val savedObservable = preference as? ObservablePreference<T>
        
        val observable = savedObservable ?: ObservablePreference(preference).apply {
            preferences[key] = this
        }
        
        return observable.observe()
    }
//    private fun <T> createNewObservable(key: String): PublishSubject<T> {
//        return PublishSubject.create<T>().apply { observables[key] = this }
//    }
}