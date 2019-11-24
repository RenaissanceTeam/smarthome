package smarthome.raspberry.devices.domain

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class AddDeviceUseCaseImpl(private val repository: DevicesRepository) : AddDeviceUseCase {
    override suspend fun execute(device: IotDevice) {
        val devices = repository.getCurrentDevices()

        if (devices.contains(device)) {
            TODO()
        }

        repository.addPendingDevice(device)
    }
}