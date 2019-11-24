package smarthome.raspberry.notification.api.domain

import smarthome.library.common.BaseController

interface ComposeDataNotificationUseCase {
    suspend fun execute(controller: BaseController, priority: Priority): Notification
}