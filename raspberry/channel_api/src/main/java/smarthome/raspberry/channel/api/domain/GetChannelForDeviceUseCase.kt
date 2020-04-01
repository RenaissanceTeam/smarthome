package smarthome.raspberry.channel.api.domain

import smarthome.raspberry.channel.api.domain.entity.DeviceChannel
import smarthome.raspberry.entity.device.Device

interface GetChannelForDeviceUseCase {
    fun execute(device: Device): DeviceChannel
}