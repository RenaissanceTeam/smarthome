package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller

interface ChangeControllerStateUseCase {
    fun execute(controller: Controller, newState: String): Controller
}