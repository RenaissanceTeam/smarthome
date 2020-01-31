package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface DeclinePendingDeviceUseCase {
    fun execute(device: Device)
}