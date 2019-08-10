package smarthome.raspberry.data.remote

interface RemoteStorageInput {
    suspend fun getUserId(): String
    suspend fun getHomeId(): String
}