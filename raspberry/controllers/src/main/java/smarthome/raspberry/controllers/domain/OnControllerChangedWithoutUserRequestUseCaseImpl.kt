package smarthome.raspberry.controllers.domain

import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase
import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.entity.controller.Controller

class OnControllerChangedWithoutUserRequestUseCaseImpl(
    private val getDeviceByControllerUseCase: GetDeviceByControllerUseCase,
    private val saveDeviceUseCase: SaveDeviceUseCase
) : OnControllerChangedWithoutUserRequestUseCase {

    override suspend fun execute(controller: Controller, newState: String) {
        // todo later here check for script conditions
        
        TODO()
//        controller.state = newState
        
        val device = getDeviceByControllerUseCase.execute(controller)
        saveDeviceUseCase.execute(device)
    }
}
