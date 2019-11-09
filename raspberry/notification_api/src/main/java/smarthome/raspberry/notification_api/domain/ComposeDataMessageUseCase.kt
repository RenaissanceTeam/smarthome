package smarthome.raspberry.notification_api.domain

import smarthome.library.common.BaseController

interface ComposeDataMessageUseCase {
    suspend fun execute(controller: BaseController, priority: Priority): Message
}