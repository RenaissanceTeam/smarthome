package smarthome.raspberry.arduinodevices.domain.mapper

import org.springframework.stereotype.Component

@Component
class ArduinoControllerTypeMapper {
    fun map(type: Int): String {
        return when (type) {
            1000 -> "analog"
            1001 -> "onoff"
            1002 -> "temperature11"
            1003 -> "humidity11"
            1004 -> "digitalAlert"
            1006 -> "temperature22"
            1007 -> "humidity22"
            1008 -> "pressure"
            else -> ""
        }
    }
}