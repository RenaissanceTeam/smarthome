package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface GetActiveDevicesUseCase {
    fun execute(): List<Device>
}