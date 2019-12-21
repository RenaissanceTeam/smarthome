package smarthome.raspberry.channel.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.channel.api.domain.arduinoChannel
import smarthome.raspberry.channel.data.ChannelRepository
import smarthome.raspberry.channel.data.ChannelRepositoryImpl
import smarthome.raspberry.channel.domain.GetChannelForDeviceUseCaseImpl

private val domain = module {
    factoryBy<GetChannelForDeviceUseCase, GetChannelForDeviceUseCaseImpl>()
}

private val data = module {
    single<ChannelRepository> {
        ChannelRepositoryImpl(
            arduinoChannel = get(named(arduinoChannel))
        )
    }
}

val channelModule = listOf(
        domain,
        data
)