package smarthome.client.domain.api.conrollers.usecases

import smarthome.client.entity.Controller

interface GetControllerUseCase {
    suspend fun execute(id: Long): Controller
}