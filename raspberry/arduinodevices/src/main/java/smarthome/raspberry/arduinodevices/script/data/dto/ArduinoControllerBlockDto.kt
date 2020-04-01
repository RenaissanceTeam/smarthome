package smarthome.raspberry.arduinodevices.script.data.dto

import smarthome.raspberry.entity.script.Position
import smarthome.raspberry.scripts.api.data.dto.BlockDto

class ArduinoControllerBlockDto(
        id: Long,
        position: Position,
        val controllerId: Long
) : BlockDto(id, position)