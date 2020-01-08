package smarthome.raspberry.entity

interface DeviceChannel {
    fun canWorkWith(type: String): Boolean
    suspend fun read()
    suspend fun write()
}