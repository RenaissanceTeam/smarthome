package smarthome.raspberry.devices.api.domain.dto

data class DeviceDTO(
    val type: String,
    val name: String = "",
    val description: String = "",
    val serialName: String,
    val controllers: List<ControllerDTO>
)