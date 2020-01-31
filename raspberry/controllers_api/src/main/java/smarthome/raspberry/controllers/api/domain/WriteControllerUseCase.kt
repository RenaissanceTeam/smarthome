package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device


interface WriteControllerUseCase {
    fun execute(controller: Controller, state: String): String
}