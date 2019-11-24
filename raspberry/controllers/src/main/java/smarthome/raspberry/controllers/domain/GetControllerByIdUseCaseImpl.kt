package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.NoControllerException
import smarthome.raspberry.devices.api.domain.GetDevicesUseCase

class GetControllerByIdUseCaseImpl(
        private val getDevicesUseCase: GetDevicesUseCase
) : GetControllerByIdUseCase {
    override suspend fun execute(id: Id): BaseController {
        val devices = getDevicesUseCase.execute()

        for (device in devices) {
            return device.controllers.find { it.id == id } ?: continue
        }
        throw NoControllerException()
    }
}