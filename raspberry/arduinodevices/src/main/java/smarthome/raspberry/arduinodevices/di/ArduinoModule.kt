package smarthome.raspberry.arduinodevices.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.library.common.DeviceChannel
import smarthome.raspberry.arduinodevices.data.server.di.serverModule
import smarthome.raspberry.arduinodevices.domain.ArduinoDeviceChannel
import smarthome.raspberry.channel.api.domain.arduinoChannel

val arduinoModule = module {
    domain
    data
    
}


private val domain = module {
    factory<DeviceChannel>(named(arduinoChannel)) { ArduinoDeviceChannel(
        httpServer = get(),
        udpServer = get()
    ) }
}

private val data = module {
    serverModule
}
