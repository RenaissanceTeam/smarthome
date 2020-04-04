package smarthome.raspberry.devices.api.domain.dto

data class ControllerDTO(
    val type: String,
    val name: String = "",
    val state: String? = null
)