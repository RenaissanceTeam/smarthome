package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.controller.Controller

interface UpdateControllerUseCase {
    fun execute(id: Long, partialUpdate: (Controller) -> Controller): Controller
}