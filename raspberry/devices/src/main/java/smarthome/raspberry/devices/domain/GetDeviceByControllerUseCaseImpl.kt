package smarthome.raspberry.devices.domain

import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device

class GetDeviceByControllerUseCaseImpl(
        private val devicesRepository: DevicesRepository
): GetDeviceByControllerUseCase {
    override suspend fun execute(controller: Controller): Device {
        TODO()
    }
}