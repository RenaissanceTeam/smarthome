package smarthome.raspberry.devices.api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

interface GetDeviceByControllerUseCase {
    suspend fun execute(controller: BaseController): IotDevice
}