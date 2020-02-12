package smarthome.client.plugingate

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.entity.condition.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.presentation.action.ArduinoActionViewResolver
import smarthome.client.arduino.presentation.conditionview.ArduinoConditionModelResolver
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionViewResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

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
    
    factory<ConditionModelResolver> {
        ConditionModelResolverImpl(listOf(
            get(named<ArduinoConditionModelResolver>())
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