package smarthome.raspberry.home.data.storage

interface LocalStorage {
    suspend fun saveHome(homeId: String)
}

