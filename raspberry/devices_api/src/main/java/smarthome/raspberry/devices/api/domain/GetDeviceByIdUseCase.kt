package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface GetDeviceByIdUseCase {
    fun execute(id: Long): Device
}