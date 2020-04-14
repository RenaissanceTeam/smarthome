package smarthome.raspberry.arduinodevices.domain.dto

data class ArduinoDeviceInit(
        val serial: Int,
        val name: String = "",
        val description: String = "",
        val services: List<ArduinoControllerDto>
)

