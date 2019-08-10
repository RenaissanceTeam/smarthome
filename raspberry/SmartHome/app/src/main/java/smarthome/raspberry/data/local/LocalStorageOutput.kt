package smarthome.raspberry.data.local

interface LocalStorageOutput {
    suspend fun createHome(homeId: String)
}