package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface GetDevicesUseCase {
    fun execute(): List<Device>
}