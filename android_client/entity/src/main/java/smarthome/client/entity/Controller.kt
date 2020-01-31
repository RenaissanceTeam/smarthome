package smarthome.client.entity

data class Controller(
    val id: Long,
    val deviceId: Long,
    val name: String,
    val type: String,
    val state: String
)
