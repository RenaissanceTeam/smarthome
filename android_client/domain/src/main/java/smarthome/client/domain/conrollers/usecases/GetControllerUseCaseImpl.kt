package smarthome.client.domain.conrollers.usecases

import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.entity.Controller

class GetControllerUseCaseImpl(
    private val repo: ControllersRepo
) : GetControllerUseCase {
    override suspend fun execute(id: Long): Controller {
        return repo.get(id)
    }
}