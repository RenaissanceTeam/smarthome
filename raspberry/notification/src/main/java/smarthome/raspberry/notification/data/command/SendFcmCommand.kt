package smarthome.raspberry.notification.data.command

import smarthome.raspberry.notification_api.domain.Message

interface SendFcmCommand {
    suspend fun send(message: Message)
}