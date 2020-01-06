package smarthome.raspberry

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.raspberry.authentication.domain.AuthenticationStateResolver
import smarthome.raspberry.channel.data.ChannelRepository
import smarthome.raspberry.home.domain.HomeStateResolver

class CreateClassesOnAppStart : KoinComponent {
    init {
        get<ChannelRepository>()
        get<AuthenticationStateResolver>()
        get<HomeStateResolver>()
    }
}