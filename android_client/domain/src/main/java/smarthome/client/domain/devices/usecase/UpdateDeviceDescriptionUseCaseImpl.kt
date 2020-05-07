package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.usecase.UpdateDeviceDescriptionUseCase
import smarthome.client.entity.Device

class UpdateDeviceDescriptionUseCaseImpl(
        private val devicesRepo: DevicesRepo
) : UpdateDeviceDescriptionUseCase {
    override suspend fun execute(deviceId: Long, description: String): Device {
        return devicesRepo.updateDescription(deviceId, description)
    }
}