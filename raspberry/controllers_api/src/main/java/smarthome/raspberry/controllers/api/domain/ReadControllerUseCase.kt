package smarthome.raspberry.controllers.api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

interface ReadControllerUseCase {
    suspend fun execute(device: IotDevice, controller: BaseController)

}