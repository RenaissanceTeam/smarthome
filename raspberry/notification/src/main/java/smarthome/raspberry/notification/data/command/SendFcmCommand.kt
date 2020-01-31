package smarthome.raspberry.notification.data.command

import smarthome.raspberry.notification.api.domain.Notification

interface SendFcmCommand {
    suspend fun send(notification: Notification)
}