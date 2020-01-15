package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface GetPendingDevicesUseCase {
    fun execute(): List<Device>
}