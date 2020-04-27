package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller

interface SaveControllerUseCase {
    fun execute(controller: Controller): Controller
}