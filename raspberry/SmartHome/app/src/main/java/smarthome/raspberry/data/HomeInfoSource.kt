package smarthome.raspberry.data

interface HomeInfoSource {
    suspend fun getUserId(): String
    suspend fun getHomeId(): String
}