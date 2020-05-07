package smarthome.client.domain.api.devices.usecase

import smarthome.client.entity.Device


interface UpdateDeviceNameUseCase {
    suspend fun execute(deviceId: Long, name: String): Device
}

