package smarthome.client.domain.conrollers.usecases

import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.WriteStateToControllerUseCase

class WriteStateToControllerUseCaseImpl(
    private val repo: ControllersRepo
) : WriteStateToControllerUseCase {
    override suspend fun execute(controllerId: Long, state: String): String {
        return repo.setState(controllerId, state)
    }
}