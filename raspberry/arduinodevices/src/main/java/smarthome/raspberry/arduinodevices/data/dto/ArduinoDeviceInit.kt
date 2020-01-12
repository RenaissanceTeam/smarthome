package smarthome.raspberry.arduinodevices.data.dto

import smarthome.raspberry.devices.api.domain.dto.ControllerDTO

data class ArduinoDeviceInit(
        val serialName: String,
        val name: String = "",
        val description: String = "",
        val type: String,
        val port: String,
        val controllers: List<ControllerDTO>
)