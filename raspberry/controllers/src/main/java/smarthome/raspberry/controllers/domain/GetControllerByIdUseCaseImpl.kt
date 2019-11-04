package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.controllers_api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers_api.domain.NoControllerException
import smarthome.raspberry.devices_api.data.DevicesRepository

class GetControllerByIdUseCaseImpl(
        private val repository: DevicesRepository
) : GetControllerByIdUseCase {
    override suspend fun execute(id: Id): BaseController {
        val devices = repository.getCurrentDevices()

        for (device in devices) {
            return device.controllers.find { it.id == id } ?: continue
        }
        throw NoControllerException()
    }
}