package smarthome.raspberry.notification.api.domain

import smarthome.raspberry.entity.Controller

interface ComposeDataNotificationUseCase {
    suspend fun execute(controller: Controller, priority: Priority): Notification
}