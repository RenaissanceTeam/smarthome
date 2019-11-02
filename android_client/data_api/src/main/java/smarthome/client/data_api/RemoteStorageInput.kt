package smarthome.client.data_api

interface RemoteStorageInput {
    suspend fun getUserId(): String
    suspend fun chooseHomeId(homeIds: MutableList<String>): String
}