package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
import smarthome.raspberry.controllers_api.data.ControllersRepository
import smarthome.raspberry.controllers_api.domain.WriteControllerUseCase
import smarthome.raspberry.devices_api.domain.DevicesService

class WriteControllerUseCaseImpl(
        private val repository: ControllersRepository,
        private val devicesService: DevicesService
) : WriteControllerUseCase {
    override suspend fun execute(device: IotDevice, controller: BaseController, state: ControllerState) {
        repository.proceedWriteController(controller, state)
        controller.serveState = ControllerServeState.IDLE

        devicesService.saveDevice(device)
    }
}