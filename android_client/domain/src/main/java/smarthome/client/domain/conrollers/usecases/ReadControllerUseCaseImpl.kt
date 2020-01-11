package smarthome.client.domain.conrollers.usecases

import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase

class ReadControllerUseCaseImpl : ReadControllerUseCase {
    override suspend fun execute(id: Long): String {
        return "todo"
    }
}