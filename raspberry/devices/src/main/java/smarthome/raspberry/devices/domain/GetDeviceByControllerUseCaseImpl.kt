package smarthome.raspberry.devices.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class GetDeviceByControllerUseCaseImpl(
        private val devicesRepository: DevicesRepository
): GetDeviceByControllerUseCase {
    override suspend fun execute(controller: BaseController): IotDevice {
        TODO()
    }
}