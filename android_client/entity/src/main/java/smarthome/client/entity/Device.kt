package smarthome.client.entity

data class Device(
    val id: Long,
    val name: String,
    val description: String,
    val type: String,
    val controllers: List<Long>
)