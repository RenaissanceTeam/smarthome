package smarthome.raspberry.scripts.data.dto.notification

import smarthome.raspberry.entity.script.Position
import smarthome.raspberry.scripts.api.data.dto.BlockDto

class NotificationBlockDto(
        id: String,
        position: Position
) : BlockDto(id, position)