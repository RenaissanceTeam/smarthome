package smarthome.raspberry.devices.domain

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.RemoveDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class RemoveDeviceUseCaseImpl(private val repository: DevicesRepository): RemoveDeviceUseCase {
    override suspend fun execute(device: IotDevice) {
        TODO()
    }
}