package smarthome.raspberry

import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.raspberry.channel.data.ChannelRepository

class CreateClassesOnAppStart : KoinComponent {
    val channelRepository: ChannelRepository by inject()
}