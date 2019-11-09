package smarthome.raspberry.notification.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices_api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.notification.R
import smarthome.raspberry.notification_api.domain.ComposeDataMessageUseCase
import smarthome.raspberry.notification_api.domain.Message
import smarthome.raspberry.notification_api.domain.Priority
import smarthome.raspberry.util.ResourceProvider

class ComposeDataMessageUseCaseImpl(private val resourceProvider: ResourceProvider,
                                    private val getDeviceByControllerUseCase: GetDeviceByControllerUseCase) :
    ComposeDataMessageUseCase {
    
    
    override suspend fun execute(controller: BaseController, priority: Priority): Message {
        val device = getDeviceByControllerUseCase.execute(controller)
        
        return Message(
            composeAlertTitle(controller),
            composeAlertBody(controller, device),
            arrayOf(""), MessageType.DATA.name, priority.name)
    }
    
    private fun composeAlertTitle(controller: BaseController): String {
        val base = resourceProvider.resources.getString(R.string.alert_trigger)
        if (controller.name.isEmpty()) return base
        return "$base: ${controller.name}"
    }
    
    private fun composeAlertBody(controller: BaseController, device: IotDevice): String {
        return resourceProvider.resources.getString(R.string.alert_body)
            .format(controller.state, device.name)
    }
}