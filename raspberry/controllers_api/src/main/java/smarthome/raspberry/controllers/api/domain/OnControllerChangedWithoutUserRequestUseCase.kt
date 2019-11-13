package smarthome.raspberry.controllers.api.domain

import smarthome.library.common.BaseController

interface OnControllerChangedWithoutUserRequestUseCase {
    suspend fun execute(controller: BaseController)
}