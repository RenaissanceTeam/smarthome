package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface GetDevicesUseCase {
    fun execute(): List<Device>
}

