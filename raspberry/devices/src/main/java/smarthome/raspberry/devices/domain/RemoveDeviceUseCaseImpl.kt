package smarthome.raspberry.devices.domain

import smarthome.raspberry.devices.api.domain.RemoveDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.device.Device

class RemoveDeviceUseCaseImpl(private val repository: DevicesRepository): RemoveDeviceUseCase {
    override suspend fun execute(device: Device) {
        TODO()
    }
}