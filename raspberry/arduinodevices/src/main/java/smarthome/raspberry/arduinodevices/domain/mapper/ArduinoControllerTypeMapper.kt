package smarthome.raspberry.arduinodevices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.domain.entity.ArduinoControllerTypes

@Component
class ArduinoControllerTypeMapper {
    fun map(type: Int) = ArduinoControllerTypes.values().find { it.id == type }?.string ?: ""
}