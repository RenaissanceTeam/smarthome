package smarthome.raspberry.controllers.domain

import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device

class WriteControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val saveDeviceUseCase: SaveDeviceUseCase
) : WriteControllerUseCase {
    override suspend fun execute(device: Device, controller: Controller, state: String) {
        TODO()
        
//        val channel = getChannelForDeviceUseCase.execute(device)
//        val newState = channel.writeState(device, controller, state)
//
//        controller.state = newState
//        controller.serveState = ControllerServeState.IDLE
//
//        saveDeviceUseCase.execute(device)
    }
}