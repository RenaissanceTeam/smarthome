package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.device.Device

interface GetDeviceBySerialUseCase {
    fun execute(serial: Int): Device?
}