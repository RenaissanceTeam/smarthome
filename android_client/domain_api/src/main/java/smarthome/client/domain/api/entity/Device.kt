package smarthome.client.domain.api.entity

data class Device(
    val id: Long,
    val name: String = "",
    val description: String = "",
    val controllers: List<Controller>
)