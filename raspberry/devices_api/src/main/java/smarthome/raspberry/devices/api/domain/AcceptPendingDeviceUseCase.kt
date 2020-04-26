package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface AcceptPendingDeviceUseCase {
    fun execute(device: Device)
}

