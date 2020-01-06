package smarthome.raspberry.notification.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.notification.api.domain.ComposeDataNotificationUseCase
import smarthome.raspberry.notification.api.domain.Notification
import smarthome.raspberry.notification.api.domain.Priority
import smarthome.raspberry.util.ResourceProvider

class ComposeDataNotificationUseCaseImpl(private val resourceProvider: ResourceProvider,
                                         private val getDeviceByControllerUseCase: GetDeviceByControllerUseCase) :
        ComposeDataNotificationUseCase {
    
    
    override suspend fun execute(controller: BaseController, priority: Priority): Notification {
        val device = getDeviceByControllerUseCase.execute(controller)
        
        return Notification(
                composeAlertTitle(controller),
                composeAlertBody(controller, device),
                arrayOf(""), NotificationType.DATA.name, priority.name)
    }
    
    private fun composeAlertTitle(controller: BaseController): String {
        val base = "Alert"
        if (controller.name.isEmpty()) return base
        return "$base: ${controller.name}"
    }
    
    private fun composeAlertBody(controller: BaseController, device: IotDevice): String {
        return "Default alert body %s %s"
            .format(controller.state, device.name)
    }
}