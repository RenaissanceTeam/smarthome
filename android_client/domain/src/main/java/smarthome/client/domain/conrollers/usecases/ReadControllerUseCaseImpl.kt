package smarthome.client.domain.conrollers.usecases

import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase

class ReadControllerUseCaseImpl(
    private val repo: ControllersRepo
) : ReadControllerUseCase {
    override suspend fun execute(id: Long): String {
        return repo.readState(id)
    }
}