package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface UpdateDeviceUseCase {
    fun execute(id: Long, partialUpdate: (Device) -> Device): Device
}