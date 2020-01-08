package smarthome.raspberry.channel.data

import smarthome.raspberry.entity.DeviceChannel


class ChannelRepositoryImpl(
    private val arduinoChannel: DeviceChannel
) : ChannelRepository {
    
    override fun getDeviceChannels(): List<DeviceChannel> {
        return listOf(arduinoChannel)
    }
}