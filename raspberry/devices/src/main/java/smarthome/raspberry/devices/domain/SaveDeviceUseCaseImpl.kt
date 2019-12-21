package smarthome.raspberry.devices.domain

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.data.storage.IotDeviceGroup

class SaveDeviceUseCaseImpl(private val repository: DevicesRepository): SaveDeviceUseCase {
    override suspend fun execute(device: IotDevice) {
        when (repository.getDeviceGroup(device)) {
            IotDeviceGroup.ACTIVE -> repository.saveDevice(device)
            IotDeviceGroup.PENDING -> repository.savePendingDevice(device)
        }
    }
}