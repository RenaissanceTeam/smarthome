package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.NoControllerException
import smarthome.raspberry.devices.data.DevicesRepository

class GetControllerByIdUseCaseImpl(
        private val repository: smarthome.raspberry.devices.data.DevicesRepository
) : GetControllerByIdUseCase {
    override suspend fun execute(id: Id): BaseController {
        val devices = repository.getCurrentDevices()

        for (device in devices) {
            return device.controllers.find { it.id == id } ?: continue
        }
        throw NoControllerException()
    }
}