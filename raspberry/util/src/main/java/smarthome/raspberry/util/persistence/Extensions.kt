package smarthome.raspberry.util.persistence


inline fun <reified T : Any> StorageHelper.get(key: String) = get(key, T::class)
suspend inline fun <reified T : Any> StorageHelper.set(key: String, value: T) {
    set(key, value, T::class)
}

inline fun <reified T: Any> PersistentStorage.get(key: String) = get(key, T::class)
