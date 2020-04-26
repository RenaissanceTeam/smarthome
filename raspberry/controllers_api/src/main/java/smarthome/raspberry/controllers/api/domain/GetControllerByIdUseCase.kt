package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller

interface GetControllerByIdUseCase {
    fun execute(id: Long): Controller
}