package smarthome.raspberry.util.persistence


inline fun <reified T : Any> SharedPreferencesHelper.get(key: String) = get(key, T::class)
suspend inline fun <reified T : Any> SharedPreferencesHelper.set(key: String, value: T) {
    set(key, value, T::class)
}

inline fun <reified T: Any> PersistentStorage.get(key: String) = get(key, T::class)
