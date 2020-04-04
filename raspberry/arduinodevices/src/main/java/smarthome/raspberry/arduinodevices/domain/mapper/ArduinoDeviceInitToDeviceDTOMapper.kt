package smarthome.raspberry.arduinodevices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.domain.dto.ArduinoDeviceInit
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO

@Component
class ArduinoDeviceInitToDeviceDTOMapper(
        private val controllerMapper: ArduinoControllerToControllerDtoMapper
) {
    fun map(arduino: ArduinoDeviceInit) = DeviceDTO(
            name = arduino.name,
            serialName = arduino.serialName,
            controllers = arduino.controllers.map(controllerMapper::map),
            description = arduino.description,
            type = arduino.type
    )
}