package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface RemoveDeviceUseCase {
    suspend fun execute(device: Device)
}