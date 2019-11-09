package smarthome.raspberry.home.data.storage

interface RemoteStorage {
    suspend fun isHomeIdUnique(homeId: String): Boolean
    suspend fun saveHome(homeId: String)
}