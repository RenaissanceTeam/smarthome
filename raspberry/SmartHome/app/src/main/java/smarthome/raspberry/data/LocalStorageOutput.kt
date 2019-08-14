package smarthome.raspberry.data

interface LocalStorageOutput {
    suspend fun createHome(homeId: String)
}