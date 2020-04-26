package smarthome.raspberry.arduinodevices.configuration

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import smarthome.raspberry.arduinodevices.controllers.data.api.ArduinoDeviceApiFactory
import smarthome.raspberry.arduinodevices.controllers.domain.dto.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.channel.domain.ArduinoDeviceChannel
import smarthome.raspberry.arduinodevices.controllers.domain.mapper.ArduinoStateMapper
import smarthome.raspberry.channel.api.domain.arduinoChannel
import smarthome.raspberry.channel.api.domain.entity.DeviceChannel

@Configuration
open class Configuration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    open fun rest(): RestOperations {
        return RestTemplate()
    }

    @Bean(arduinoChannel)
    open fun deviceChannel(addressRepository: ArduinoDeviceAddressRepository,
                           arduinoDeviceApiFactory: ArduinoDeviceApiFactory,
                           stateMapper: ArduinoStateMapper
    ): DeviceChannel {
        return ArduinoDeviceChannel(addressRepository, arduinoDeviceApiFactory, stateMapper)
    }
}