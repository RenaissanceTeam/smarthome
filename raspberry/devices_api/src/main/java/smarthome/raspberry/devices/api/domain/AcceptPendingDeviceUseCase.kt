package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface AcceptPendingDeviceUseCase {
    fun execute(device: Device)
}

