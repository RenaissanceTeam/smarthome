package smarthome.raspberry.channel.api.domain

import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice

interface GetChannelForDeviceUseCase {
    fun execute(device: IotDevice): DeviceChannel
}