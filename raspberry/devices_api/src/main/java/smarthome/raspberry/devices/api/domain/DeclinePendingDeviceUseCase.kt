package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface DeclinePendingDeviceUseCase {
    fun execute(device: Device)
}