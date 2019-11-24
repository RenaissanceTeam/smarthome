package smarthome.raspberry.devices.domain

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class SaveDeviceUseCaseImpl(private val repository: DevicesRepository): SaveDeviceUseCase {
    override suspend fun execute(device: IotDevice) {
        repository.saveDevice(device)
    }
}