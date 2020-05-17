package smarthome.raspberry.scripts.notification.dto

import smarthome.raspberry.scripts.api.data.dto.ActionDto

class SendNotificationActionDto (
        id: String,
        val user: String,
        val message: String
) : ActionDto(id)