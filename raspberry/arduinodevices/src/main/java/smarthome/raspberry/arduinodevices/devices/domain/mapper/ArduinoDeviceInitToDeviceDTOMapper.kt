package smarthome.raspberry.arduinodevices.devices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.devices.domain.dto.ArduinoDeviceInit
import smarthome.raspberry.arduinodevices.controllers.domain.mapper.ArduinoControllerToControllerDtoMapper
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