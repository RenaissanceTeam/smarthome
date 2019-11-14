package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.devices.api.domain.DevicesService
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase

class WriteControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val saveDeviceUseCase: SaveDeviceUseCase
) : WriteControllerUseCase {
    override suspend fun execute(device: IotDevice, controller: BaseController, state: ControllerState) {
        val channel = getChannelForDeviceUseCase.execute(device)
        val newState = channel.writeState(device, controller, state)

        controller.state = newState
        controller.serveState = ControllerServeState.IDLE

        saveDeviceUseCase.execute(device)
    }
}