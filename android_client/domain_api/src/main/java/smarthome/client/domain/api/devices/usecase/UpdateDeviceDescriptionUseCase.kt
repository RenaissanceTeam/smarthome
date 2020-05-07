package smarthome.client.domain.api.devices.usecase

import smarthome.client.entity.Device

interface UpdateDeviceDescriptionUseCase {
    suspend fun execute(deviceId: Long, description: String): Device
}