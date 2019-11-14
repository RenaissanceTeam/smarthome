package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.IotDevice
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.devices.api.domain.DevicesService
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase

class ReadControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val saveDeviceUseCase: SaveDeviceUseCase) :
        ReadControllerUseCase {

    override suspend fun execute(device: IotDevice, controller: BaseController) {
        val channel = getChannelForDeviceUseCase.execute(device)
        val newState = channel.read(device, controller)

        controller.state = newState
        controller.serveState = ControllerServeState.IDLE

        saveDeviceUseCase.execute(device)
    }
}