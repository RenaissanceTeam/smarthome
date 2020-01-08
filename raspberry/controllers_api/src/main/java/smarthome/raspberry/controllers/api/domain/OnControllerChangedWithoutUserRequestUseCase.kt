package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.Controller


interface OnControllerChangedWithoutUserRequestUseCase {
    suspend fun execute(controller: Controller, newState: String)
}