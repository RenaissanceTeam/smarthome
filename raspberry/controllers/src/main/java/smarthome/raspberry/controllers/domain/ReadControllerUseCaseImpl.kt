package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.IotDevice
import smarthome.raspberry.api.channel.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.devices_api.domain.DevicesService

class ReadControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val devicesService: DevicesService) :
        ReadControllerUseCase {

    override suspend fun execute(device: IotDevice, controller: BaseController) {
        val channel = getChannelForDeviceUseCase.execute(device)
        val newState = channel.read(device, controller)

        controller.state = newState
        controller.serveState = ControllerServeState.IDLE

        devicesService.saveDevice(device)
    }
}