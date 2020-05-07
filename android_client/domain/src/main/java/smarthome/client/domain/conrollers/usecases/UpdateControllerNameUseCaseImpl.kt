package smarthome.client.domain.conrollers.usecases

import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.UpdateControllerNameUseCase

class UpdateControllerNameUseCaseImpl(
        private val controllersRepo: ControllersRepo
) : UpdateControllerNameUseCase {
    override suspend fun execute(id: Long, name: String) {
        controllersRepo.updateName(id, name)
    }
}