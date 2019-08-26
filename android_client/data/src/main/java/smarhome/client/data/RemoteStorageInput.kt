package smarhome.client.data

interface RemoteStorageInput {
    suspend fun getUserId(): String
    suspend fun chooseHomeId(homeIds: MutableList<String>): String
}