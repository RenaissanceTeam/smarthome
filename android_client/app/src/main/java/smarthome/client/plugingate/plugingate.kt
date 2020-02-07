package smarthome.client.plugingate

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.entity.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.entity.block.ArduinoControllerBlockResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver

val plugingate = module {
    factory<ConditionFromBlockResolver> {
        ConditionFromBlockResolverImpl(listOf(
            get(named<ArduinoConditionFromBlockResolver>()
            ))
        )
    }
    
    factory<ControllerBlockResolver> {
        ControllerBlockResolverImpl(listOf(
            get(named<ArduinoControllerBlockResolver>())
        ))
    }
}