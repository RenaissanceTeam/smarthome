package smarthome.raspberry.home.data.storage

interface LocalStorage {
    fun saveHome(homeId: String)

}

interface RemoteStorage {
    suspend fun isHomeIdUnique(homeId: String): Boolean
    suspend fun saveHome(homeId: String)
}