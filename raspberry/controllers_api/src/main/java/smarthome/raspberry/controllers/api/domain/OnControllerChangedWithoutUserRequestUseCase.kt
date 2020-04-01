package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller


interface OnControllerChangedWithoutUserRequestUseCase {
    suspend fun execute(controller: Controller, newState: String)
}