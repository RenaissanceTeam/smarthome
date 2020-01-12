package smarthome.raspberry.arduinodevices.di

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate

@Configuration
class Configuration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun rest(): RestOperations {
        return RestTemplate()
    }
}