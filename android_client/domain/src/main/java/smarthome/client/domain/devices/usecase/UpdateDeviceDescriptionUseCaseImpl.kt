package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.usecase.UpdateDeviceDescriptionUseCase

class UpdateDeviceDescriptionUseCaseImpl(
        private val devicesRepo: DevicesRepo
) : UpdateDeviceDescriptionUseCase {
    override suspend fun execute(deviceId: Long, description: String) {
        devicesRepo.updateDescription(deviceId, description)
    }
}