package smarthome.raspberry.scripts.location.dto

import smarthome.raspberry.entity.script.Position
import smarthome.raspberry.scripts.api.data.dto.BlockDto

class LocationBlockDto(
        id: String,
        position: Position
) : BlockDto(id, position)