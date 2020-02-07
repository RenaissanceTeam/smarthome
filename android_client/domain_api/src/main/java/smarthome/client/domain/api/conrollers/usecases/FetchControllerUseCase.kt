package smarthome.client.domain.api.conrollers.usecases

import smarthome.client.entity.Controller

interface FetchControllerUseCase {
    suspend fun execute(id: Long): Controller
}