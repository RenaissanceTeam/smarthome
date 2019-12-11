package smarthome.raspberry.util.persistence.sharedprefs

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import smarthome.raspberry.util.persistence.NoStoredPreference
import smarthome.raspberry.util.persistence.PersistentStorage
import kotlin.reflect.KClass

class SharedPreferencesAdapter(private val prefs: SharedPreferences) :
    PersistentStorage {
    override suspend fun <T : Any> set(key: String, value: T) {
        when (value) {
            is String -> editOnIo { it.putString(key, value) }
            is Boolean -> editOnIo { it.putBoolean(key, value) }
            is Int -> editOnIo { it.putInt(key, value) }
            is Float -> editOnIo { it.putFloat(key, value) }
            is Long -> editOnIo { it.putLong(key, value) }
            else -> throw IllegalArgumentException("shared prefs can't support value of type ${value::class}")
        }
    }
    
    override fun <T : Any> get(key: String, typeParam: KClass<T>): T {
        if (!prefs.contains(key)) throw NoStoredPreference(key)
        
        return when (typeParam) {
            String::class -> prefs.getString(key, "") as T
            Boolean ::class -> prefs.getBoolean(key, false) as T
            Int ::class -> prefs.getInt(key, 0) as T
            Float ::class -> prefs.getFloat(key, 0f) as T
            Long ::class -> prefs.getLong(key, 0L) as T
            else -> throw IllegalArgumentException("shared prefs can't support type $typeParam")
        }
    }
    
    private suspend fun editOnIo(block: (SharedPreferences.Editor) -> SharedPreferences.Editor) {
        withContext(Dispatchers.IO) {
            block.invoke(prefs.edit()).commit()
        }
    }
    
    companion object {
        internal const val PREFS_NAME = "smarthome"
    }
}



