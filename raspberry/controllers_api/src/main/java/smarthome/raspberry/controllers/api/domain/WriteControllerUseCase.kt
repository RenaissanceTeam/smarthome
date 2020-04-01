package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller


interface WriteControllerUseCase {
    fun execute(controller: Controller, state: String): String
}