package smarthome.raspberry.notification.api.domain.usecase

import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.notification.api.domain.entity.Notification
import smarthome.raspberry.notification.api.domain.entity.Priority

interface ComposeDataNotificationUseCase {
    suspend fun execute(controller: Controller, priority: Priority): Notification
}