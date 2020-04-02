package smarthome.raspberry.arduinodevices.script.data

import smarthome.raspberry.entity.script.Position
import smarthome.raspberry.scripts.api.data.dto.BlockDto

class ArduinoControllerBlockDto(
        id: String,
        position: Position,
        val controllerId: Long
) : BlockDto(id, position)