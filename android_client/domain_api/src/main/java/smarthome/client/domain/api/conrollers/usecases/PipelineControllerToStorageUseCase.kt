package smarthome.client.domain.api.conrollers.usecases

import smarthome.client.entity.Controller

interface PipelineControllerToStorageUseCase {
    fun execute(controller: Controller)
}