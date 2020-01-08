package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface GetDeviceByIdUseCase {
    suspend fun execute(id: Long): Device
}