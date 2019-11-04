package smarthome.raspberry.channel_api.domain

import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice

interface GetChannelForDeviceUseCase {
    suspend fun execute(device: IotDevice): DeviceChannel
}