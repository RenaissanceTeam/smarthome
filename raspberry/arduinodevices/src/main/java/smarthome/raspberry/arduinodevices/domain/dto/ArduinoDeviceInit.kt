package smarthome.raspberry.arduinodevices.domain.dto

data class ArduinoDeviceInit(
        val serialName: String,
        val name: String = "",
        val description: String = "",
        val type: String,
        val controllers: List<ArduinoControllerDto>
)

