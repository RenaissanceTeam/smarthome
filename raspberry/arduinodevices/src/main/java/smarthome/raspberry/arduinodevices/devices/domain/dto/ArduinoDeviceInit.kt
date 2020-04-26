package smarthome.raspberry.arduinodevices.devices.domain.dto

import smarthome.raspberry.arduinodevices.controllers.domain.dto.ArduinoControllerDto

data class ArduinoDeviceInit(
        val serial: Int,
        val name: String = "",
        val description: String = "",
        val services: List<ArduinoControllerDto>
)

