package smarthome.raspberry.channel.api.domain

import smarthome.raspberry.entity.Device
import smarthome.raspberry.entity.DeviceChannel

interface GetChannelForDeviceUseCase {
    fun execute(device: Device): DeviceChannel
}