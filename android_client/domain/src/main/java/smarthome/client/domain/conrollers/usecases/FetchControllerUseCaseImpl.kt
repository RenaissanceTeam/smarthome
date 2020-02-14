package smarthome.client.domain.conrollers.usecases

import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.FetchControllerUseCase
import smarthome.client.entity.Controller

class FetchControllerUseCaseImpl(
    private val repo: ControllersRepo
) : FetchControllerUseCase {
    override suspend fun execute(id: Long): Controller {
        return repo.fetch(id)
    }
}