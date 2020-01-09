package smarthome.client.entity

data class Controller(
    val id: Long,
    val name: String,
    val type: String,
    val state: State
)
