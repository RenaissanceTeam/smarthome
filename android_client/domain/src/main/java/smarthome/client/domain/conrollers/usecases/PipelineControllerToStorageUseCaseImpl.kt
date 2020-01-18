package smarthome.client.domain.conrollers.usecases

import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.PipelineControllerToStorageUseCase
import smarthome.client.entity.Controller

class PipelineControllerToStorageUseCaseImpl(
    private val controllersRepo: ControllersRepo
) : PipelineControllerToStorageUseCase {
    
    override fun execute(controller: Controller) {
        controllersRepo.controllerUpdated(controller)
    }
}