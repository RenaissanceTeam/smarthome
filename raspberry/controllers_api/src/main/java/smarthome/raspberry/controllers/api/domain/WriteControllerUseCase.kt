package smarthome.raspberry.controllers.api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice

interface WriteControllerUseCase {
    suspend fun execute(device: IotDevice, controller: BaseController, state: ControllerState)
}