package smarthome.raspberry.devices.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices_api.data.DevicesRepository
import smarthome.raspberry.devices_api.domain.GetDeviceByControllerUseCase

class GetDeviceByControllerUseCaseImpl(
        private val devicesRepository: DevicesRepository
): GetDeviceByControllerUseCase {
    override suspend fun execute(controller: BaseController): IotDevice {
        return devicesRepository.getCurrentDevices().find { it.controllers.contains(controller) } ?: TODO()
    }
}