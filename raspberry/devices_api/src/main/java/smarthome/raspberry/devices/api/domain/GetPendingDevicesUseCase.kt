package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface GetPendingDevicesUseCase {
    fun execute(): List<Device>
}