package smarthome.raspberry.channel.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.channel.api.domain.NoChannelException
import smarthome.raspberry.channel.data.ChannelRepository
import smarthome.raspberry.entity.Device
import smarthome.raspberry.entity.DeviceChannel

@Component
class GetChannelForDeviceUseCaseImpl(
        private val repository: ChannelRepository
) : GetChannelForDeviceUseCase {
    override fun execute(device: Device): DeviceChannel {
        val channels = repository.getDeviceChannels()
        
        return channels.find { it.canWorkWith(device.type) } ?: throw NoChannelException(device)
    }
}