package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device


interface ReadControllerUseCase {
    fun execute(controller: Controller): String
}