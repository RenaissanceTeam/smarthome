package smarthome.raspberry.devices_api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

interface GetDeviceByControllerUseCase {
    suspend fun execute(controller: BaseController): IotDevice
}