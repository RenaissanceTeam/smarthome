package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface GetDevicesUseCase {
    suspend fun execute(): List<Device>
}