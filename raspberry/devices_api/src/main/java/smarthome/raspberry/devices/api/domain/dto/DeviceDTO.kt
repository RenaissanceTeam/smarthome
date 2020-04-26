package smarthome.raspberry.devices.api.domain.dto

data class DeviceDTO(
        val type: String,
        val name: String = "",
        val description: String = "",
        val serial: Int,
        val controllers: List<ControllerDTO>
)