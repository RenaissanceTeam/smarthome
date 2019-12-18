package smarthome.raspberry

import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.raspberry.authentication.domain.AuthenticationStateResolver
import smarthome.raspberry.channel.data.ChannelRepository
import smarthome.raspberry.home.domain.HomeStateResolver

class CreateClassesOnAppStart : KoinComponent {
    val channelRepository: ChannelRepository by inject()
    val authenticationStateResolver: AuthenticationStateResolver by inject()
    val homeStateResolver: HomeStateResolver by inject()
}