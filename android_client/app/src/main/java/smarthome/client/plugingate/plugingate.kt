package smarthome.client.plugingate

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.scripts.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.scripts.entity.block.ArduinoBlockNameResolver
import smarthome.client.arduino.scripts.entity.condition.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.scripts.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.scripts.presentation.action.ArduinoActionModelResolver
import smarthome.client.arduino.scripts.presentation.condition.ArduinoConditionModelResolver
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
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
    
    factory<ActionModelResolver> {
        ActionModelResolverImpl(listOf(
            get(named<ArduinoActionModelResolver>())
        ))
    }
    factory<BlockNameResolver> {
        BlockNameResolverImp(listOf(
            get(named<ArduinoBlockNameResolver>())
        ))
    }
}