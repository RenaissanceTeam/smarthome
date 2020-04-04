package smarthome.raspberry.arduinodevices.domain.mapper

import org.springframework.stereotype.Component

@Component
class ArduinoControllerTypeMapper {
    fun map(type: Int): String {
        return when (type) {
            1001 -> ""
            else -> ""
        }
    }
}