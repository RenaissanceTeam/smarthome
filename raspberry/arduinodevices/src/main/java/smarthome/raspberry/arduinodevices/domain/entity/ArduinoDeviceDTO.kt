package smarthome.raspberry.arduinodevices.domain.entity

data class ArduinoDeviceDTO(
    val type: String,
    val name: String,
    val controllers: List<ControllerDTO>
)

data class ControllerDTO(
    val type: String,
    val name: String,
    val state: StateDTO
)

data class StateDTO(
    val type: String,
    val value: String
)