package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.IotDevice
import smarthome.raspberry.controllers_api.data.ControllersRepository
import smarthome.raspberry.controllers_api.domain.ReadControllerUseCase
import smarthome.raspberry.devices_api.domain.DevicesService

class ReadControllerUseCaseImpl(private val repository: ControllersRepository,
                                private val devicesService: DevicesService): ReadControllerUseCase {
    override suspend fun execute(device: IotDevice, controller: BaseController) {
            repository.proceedReadController(controller)
            controller.serveState = ControllerServeState.IDLE

        devicesService.saveDevice(device)
    }
}