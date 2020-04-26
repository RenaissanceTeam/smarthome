package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller


interface ReadControllerUseCase {
    fun execute(controller: Controller): String
}