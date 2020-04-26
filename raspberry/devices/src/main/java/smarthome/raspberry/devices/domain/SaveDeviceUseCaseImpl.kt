package smarthome.raspberry.devices.domain

import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.device.Device

class SaveDeviceUseCaseImpl(private val repository: DevicesRepository): SaveDeviceUseCase {
    override suspend fun execute(device: Device) {
        TODO()
    }
}