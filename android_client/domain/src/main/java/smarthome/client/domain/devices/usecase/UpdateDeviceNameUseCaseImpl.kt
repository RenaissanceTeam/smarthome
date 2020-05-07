package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.usecase.UpdateDeviceNameUseCase
import smarthome.client.entity.Device

class UpdateDeviceNameUseCaseImpl(
        private val devicesRepo: DevicesRepo
) : UpdateDeviceNameUseCase {
    override suspend fun execute(deviceId: Long, name: String): Device {
        return devicesRepo.updateName(deviceId, name)
    }
}