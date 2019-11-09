package smarthome.raspberry.channel_api.data

import smarthome.library.common.DeviceChannel

interface ChannelRepository {
    fun getDeviceChannels(): Map<String, DeviceChannel>
}