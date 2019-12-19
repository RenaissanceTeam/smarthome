package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase
import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase

class OnControllerChangedWithoutUserRequestUseCaseImpl(
    private val getDeviceByControllerUseCase: GetDeviceByControllerUseCase,
    private val saveDeviceUseCase: SaveDeviceUseCase
) : OnControllerChangedWithoutUserRequestUseCase {

    override suspend fun execute(controller: BaseController, newState: ControllerState) {
        // todo later here check for script conditions
        
        controller.state = newState
        
        val device = getDeviceByControllerUseCase.execute(controller)
        saveDeviceUseCase.execute(device)
    }
}
