package smarthome.raspberry.scripts.api.data

interface RegisteredProtocolsRepository {
    fun register(scriptId: Long)
    fun unregister(scriptId: Long)
}