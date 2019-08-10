package smarthome.raspberry.data.local

interface LocalStorageInput {
    suspend fun generateHomeId(): String

}