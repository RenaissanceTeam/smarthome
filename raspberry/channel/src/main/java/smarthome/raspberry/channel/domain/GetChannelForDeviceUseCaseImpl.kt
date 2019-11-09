package smarthome.raspberry.channel.domain

import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice
import smarthome.raspberry.channel_api.data.ChannelRepository
import smarthome.raspberry.channel_api.domain.GetChannelForDeviceUseCase

class GetChannelForDeviceUseCaseImpl(
        private val repository: ChannelRepository
) : GetChannelForDeviceUseCase {
    override suspend fun execute(device: IotDevice): DeviceChannel {
        return repository.getDeviceChannels()[device.javaClass.simpleName]
                ?: throw IllegalArgumentException("no channel for $device")
    }
}