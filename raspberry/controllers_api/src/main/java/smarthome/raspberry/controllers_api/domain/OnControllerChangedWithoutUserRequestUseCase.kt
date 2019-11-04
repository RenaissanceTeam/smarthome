package smarthome.raspberry.controllers_api.domain

import smarthome.library.common.BaseController

interface OnControllerChangedWithoutUserRequestUseCase {
    suspend fun execute(controller: BaseController)
}