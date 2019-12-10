package smarthome.raspberry.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass


class SharedPreferencesHelper(context: Context) {
    private val preferences = mutableMapOf<String, Preference<*>>()
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("smarthome", MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    suspend fun setString(key: String, value: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(key, value).commit()
        }
    }

    fun <T : Any> set(key: String, value: T) {
        preferences[key] = Preference(key, value)
    }
    
    inline fun <reified T: Any> get(key: String) = get(key, T::class)
    
    fun <T : Any> get(key: String, expectedType: KClass<out T>): T {
        val preference = preferences[key] ?: throw IllegalArgumentException(
            "can't read key=$key as it is not stored")
        val saved = preference.get()
        
        try {
            return expectedType.java.cast(saved)
                ?: throw IllegalStateException("value for key=$key is null")
        } catch (e: ClassCastException) {
            throw IllegalArgumentException(
                "can'r read key=$key as saved type (${saved::class}) doesn't match expected ($expectedType)")
        }
        
    }
    
    fun getString(key: String, default: String = ""): String {
        return sharedPreferences.getString(key, default) ?: default
    }

//    fun <T> observe(key: String): Observable<T> {
//        return observables[key] ?: createNewObservable<T>(key)
//    }
//
//    private fun <T> createNewObservable(key: String): PublishSubject<T> {
//        return PublishSubject.create<T>().apply { observables[key] = this }
//    }
}

private class Preference<T : Any>(key: String, default: T) {
    private var value = default
    
    fun get() = value
    
    fun set(value: T) {
        this.value = value
    }
}

private class ObservablePreference<T>(key: String) {
    private val subject = BehaviorSubject.create<T>()
    
    fun observe(): Observable<T> {
        return subject
    }
    
    fun getValue(default: T): T = subject.value ?: default
}