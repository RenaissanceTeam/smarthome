package smarthome.raspberry.arduinodevices.di

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApiFactory
import smarthome.raspberry.arduinodevices.domain.ArduinoDeviceChannel
import smarthome.raspberry.channel.api.domain.arduinoChannel
import smarthome.raspberry.entity.DeviceChannel

@Configuration
open class Configuration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    open fun rest(): RestOperations {
        return RestTemplate()
    }

    @Bean(arduinoChannel)
    open fun deviceChannel(addressRepository: ArduinoDeviceAddressRepository,
                      arduinoDeviceApiFactory: ArduinoDeviceApiFactory
                      ): DeviceChannel {
        return ArduinoDeviceChannel(addressRepository, arduinoDeviceApiFactory)
    }
}