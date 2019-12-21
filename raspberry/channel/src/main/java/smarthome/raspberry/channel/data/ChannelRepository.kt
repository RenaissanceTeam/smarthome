package smarthome.raspberry.channel.data

import smarthome.library.common.DeviceChannel

interface ChannelRepository {
    fun getDeviceChannels(): List<DeviceChannel>
}