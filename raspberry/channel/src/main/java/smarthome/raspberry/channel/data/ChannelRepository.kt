package smarthome.raspberry.channel.data

import smarthome.raspberry.entity.DeviceChannel


interface ChannelRepository {
    fun getDeviceChannels(): List<DeviceChannel>
}