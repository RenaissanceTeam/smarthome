package smarthome.client.domain.api.conrollers.usecases

import smarthome.client.entity.Controller

interface GetControllerUseCase {
    fun execute(id: Long): Controller
}