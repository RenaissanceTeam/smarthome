package smarthome.raspberry.channel.domain

import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.channel.api.domain.NoChannelException
import smarthome.raspberry.channel.data.ChannelRepository

class GetChannelForDeviceUseCaseImpl(
        private val repository: ChannelRepository
) : GetChannelForDeviceUseCase {
    override fun execute(device: IotDevice): DeviceChannel {
        val channels = repository.getDeviceChannels()
        
        return channels.find { it.canWorkWith(device) } ?: throw NoChannelException(device)
    }
}