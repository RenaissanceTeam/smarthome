package smarthome.client.domain.api.devices.usecase

import smarthome.client.entity.Device

interface GetDeviceUseCase {
    suspend fun execute(deviceId: Long): Device
}