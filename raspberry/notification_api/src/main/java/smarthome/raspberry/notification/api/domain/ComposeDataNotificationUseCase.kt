package smarthome.raspberry.notification.api.domain

import smarthome.raspberry.entity.controller.Controller

interface ComposeDataNotificationUseCase {
    suspend fun execute(controller: Controller, priority: Priority): Notification
}