package smarthome.client.domain.api.entity

data class Controller(
    val id: Long,
    val name: String,
    val type: String,
    val state: State
)
