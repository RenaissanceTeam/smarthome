package smarthome.raspberry.channel.data

import smarthome.raspberry.channel.api.domain.entity.DeviceChannel


interface ChannelRepository {
    fun getDeviceChannels(): List<DeviceChannel>
}