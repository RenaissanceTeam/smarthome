package smarthome.raspberry.channel.data

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import smarthome.raspberry.channel.api.domain.arduinoChannel
import smarthome.raspberry.entity.DeviceChannel

@Component
class ChannelRepositoryImpl(
        @Qualifier(arduinoChannel) private val arduino: DeviceChannel
) : ChannelRepository {

    override fun getDeviceChannels(): List<DeviceChannel> {
        return listOf(arduino)
    }
}