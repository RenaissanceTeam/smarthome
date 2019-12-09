package smarthome.raspberry.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass


class SharedPreferencesHelper(context: Context) {
    private val observables = mutableMapOf<String, PublishSubject<Any>>()
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("smarthome", MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    suspend fun setString(key: String, value: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(key, value).commit()
        }
    }

    fun getString(key: String, default: String = ""): String {
        return sharedPreferences.getString(key, default) ?: default
    }
    
    fun <T> observe(key: String): Observable<T> {
        val observable = observables[key] ?:
    }
    
    private fun <T> createNewObservable(key: String): PublishSubject<T> {
        return PublishSubject.create<T>().apply { observables[key] = this }
    }
}