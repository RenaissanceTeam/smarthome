package smarthome.raspberry.arduinodevices.controllers.data.dto

data class ArduinoUpdateRequest(
        val serial: Int,
        val state: String
)