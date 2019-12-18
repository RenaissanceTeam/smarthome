package smarthome.raspberry.channel.data

import smarthome.library.common.DeviceChannel

class ChannelRepositoryImpl(
    private val arduinoChannel: DeviceChannel
) : ChannelRepository {
    
    override fun getDeviceChannels(): List<DeviceChannel> {
        return listOf(arduinoChannel)
    }
}