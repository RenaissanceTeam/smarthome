package smarthome.raspberry.controllers_api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice

interface ReadControllerUseCase {
    suspend fun execute(device: IotDevice, controller: BaseController)

}