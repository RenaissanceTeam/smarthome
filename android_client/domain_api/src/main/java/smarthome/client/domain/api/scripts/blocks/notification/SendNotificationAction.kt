package smarthome.client.domain.api.scripts.blocks.notification

import smarthome.client.entity.script.dependency.action.Action

data class SendNotificationAction(
        override val id: String,
        val user: String? = null,
        val message: String? = null
) : Action()