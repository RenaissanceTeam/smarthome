package smarthome.raspberry.arduinodevices.controllers.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.controllers.domain.dto.ArduinoControllerDto
import smarthome.raspberry.devices.api.domain.dto.ControllerDTO

@Component
class ArduinoControllerToControllerDtoMapper(
        private val typeMapper: ArduinoControllerTypeMapper
) {
    fun map(arduino: ArduinoControllerDto) = ControllerDTO(
            type = typeMapper.map(arduino.type),
            serial = arduino.serial
    )
}