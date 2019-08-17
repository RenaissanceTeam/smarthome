package smarthome.client.domain.usecases

import smarthome.client.domain.HomeRepository
import smarthome.client.domain.NoControllerException
import smarthome.library.common.BaseController

class PendingControllersUseCase(private val repository: HomeRepository) {
    suspend fun getPendingController(controllerGuid: Long): BaseController {
        return repository.getPendingController(controllerGuid) ?: throw NoControllerException(controllerGuid)
    }

}