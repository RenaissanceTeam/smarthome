package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase

class OnControllerChangedWithoutUserRequestUseCaseImpl(
) : OnControllerChangedWithoutUserRequestUseCase {

    override suspend fun execute(controller: BaseController, newState: ControllerState) {
        // todo react to controller change
    }
}
