package smarthome.raspberry.data

interface LocalStorageInput {
    suspend fun generateHomeId(): String

}