package smarthome.raspberry.scripts.notification.dto

import smarthome.raspberry.entity.script.Position
import smarthome.raspberry.scripts.api.data.dto.BlockDto

class NotificationBlockDto(
        id: String,
        position: Position
) : BlockDto(id, position)