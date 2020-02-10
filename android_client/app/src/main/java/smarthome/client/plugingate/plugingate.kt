package smarthome.client.plugingate

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.entity.block.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.presentation.action.ArduinoActionViewResolver
import smarthome.client.arduino.presentation.conditionview.ArduinoConditionViewResolver
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionViewResolver
import smarthome.client.presentation.scripts.resolver.ConditionViewResolver

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
    
    factory<ConditionViewResolver> {
        ConditionViewResolverImpl(listOf(
            get(named<ArduinoConditionViewResolver>())
        ))
    }
    
    factory<ActionFromBlockResolver> {
        ActionFromBlockResolverImpl(listOf(
            get(named<ArduinoActionFromBlockResolver>())
        ))
    }
    
    factory<ActionViewResolver> {
        ActionViewResolverImpl(listOf(
            get(named<ArduinoActionViewResolver>())
        ))
    }
    
    
}