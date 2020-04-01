package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface SaveDeviceUseCase {
    suspend fun execute(device: Device)
}