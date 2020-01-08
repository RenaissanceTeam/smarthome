package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device


interface WriteControllerUseCase {
    suspend fun execute(device: Device, controller: Controller, state: String)
}