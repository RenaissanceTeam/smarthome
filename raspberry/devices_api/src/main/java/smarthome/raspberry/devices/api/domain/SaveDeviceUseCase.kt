package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface SaveDeviceUseCase {
    suspend fun execute(device: Device)
}