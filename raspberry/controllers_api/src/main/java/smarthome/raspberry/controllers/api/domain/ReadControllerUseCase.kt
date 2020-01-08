package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device


interface ReadControllerUseCase {
    suspend fun execute(controller: Controller)
}