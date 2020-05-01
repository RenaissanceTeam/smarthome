package smarthome.raspberry.notification.domain

import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.entity.device.Device
import smarthome.raspberry.notification.api.domain.usecase.ComposeDataNotificationUseCase
import smarthome.raspberry.notification.api.domain.entity.Notification
import smarthome.raspberry.notification.api.domain.entity.Priority

class ComposeDataNotificationUseCaseImpl(
    private val getDeviceByControllerUseCase: GetDeviceByControllerUseCase) :
        ComposeDataNotificationUseCase {
    
    
    override suspend fun execute(controller: Controller, priority: Priority): Notification {
        val device = getDeviceByControllerUseCase.execute(controller)
        
        return Notification(
                composeAlertTitle(controller),
                composeAlertBody(controller, device),
                arrayOf(""), NotificationType.DATA.name, priority.name)
    }
    
    private fun composeAlertTitle(controller: Controller): String {
        val base = "Alert"
        if (controller.name.isEmpty()) return base
        return "$base: ${controller.name}"
    }
    
    private fun composeAlertBody(controller: Controller, device: Device): String {
        return "Default alert body %s %s"
            .format(controller.state, device.name)
    }
}