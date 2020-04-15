package smarthome.raspberry.arduinodevices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.domain.entity.ArduinoControllerTypes
import smarthome.raspberry.entity.controller.Controller

@Component
class ArduinoStateMapper {
    fun mapFromRaw(controller: Controller, raw: String): String {
        return when (controller.type) {
            ArduinoControllerTypes.onoff.string -> {
                when (raw) {
                    "0" -> "off"
                    "1" -> "on"
                    else -> throw RuntimeException("Can't map onoff raw $raw")
                }
            }
            else -> raw
        }
    }

    fun mapToRaw(controller: Controller, state: String): String {
        return when (controller.type) {
            ArduinoControllerTypes.onoff.string -> {
                when (state) {
                    "on" -> "1"
                    "off" -> "0"
                    else -> throw RuntimeException("Can't map onoff state $state")
                }
            }
            else -> state
        }
    }
}