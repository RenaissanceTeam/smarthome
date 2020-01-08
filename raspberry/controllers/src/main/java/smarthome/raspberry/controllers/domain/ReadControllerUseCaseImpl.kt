package smarthome.raspberry.controllers.domain

import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.entity.Controller

class ReadControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val saveDeviceUseCase: SaveDeviceUseCase) :
        ReadControllerUseCase {

    override suspend fun execute(controller: Controller) {
        TODO()
        
        
//        val channel = getChannelForDeviceUseCase.execute(device)
//        val newState = channel.read(device, controller)
//
//        controller.state = newState
//        controller.serveState = ControllerServeState.IDLE
//
//        saveDeviceUseCase.execute(device)
    }
}