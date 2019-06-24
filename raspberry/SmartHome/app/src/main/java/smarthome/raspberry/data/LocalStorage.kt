package smarthome.raspberry.data

interface LocalStorage {
    suspend fun getHomeId(): String
}