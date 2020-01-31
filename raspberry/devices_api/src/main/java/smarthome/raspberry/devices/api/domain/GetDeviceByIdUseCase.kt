package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Device

interface GetDeviceByIdUseCase {
    fun execute(id: Long): Device
}