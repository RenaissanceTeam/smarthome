package smarthome.raspberry.scripts.data.dto.notification

import smarthome.raspberry.scripts.api.data.dto.ActionDto

class SendNotificationActionDto (
        id: String,
        val user: String,
        val message: String
) : ActionDto(id)