package smarthome.raspberry.controllers.api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState

interface OnControllerChangedWithoutUserRequestUseCase {
    suspend fun execute(controller: BaseController, newState: ControllerState)
}