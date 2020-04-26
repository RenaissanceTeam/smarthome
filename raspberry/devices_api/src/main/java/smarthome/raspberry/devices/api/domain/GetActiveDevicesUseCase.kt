package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface GetActiveDevicesUseCase {
    fun execute(): List<Device>
}