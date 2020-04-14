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
            serial = arduino.serial,
            controllers = arduino.services.map(controllerMapper::map),
            description = arduino.description,
            type = "arduino"
    )
}