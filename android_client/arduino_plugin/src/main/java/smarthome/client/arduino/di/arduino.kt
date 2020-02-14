package smarthome.client.arduino.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.entity.block.ArduinoBlockNameResolver
import smarthome.client.arduino.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.entity.condition.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.presentation.action.ArduinoActionModelResolver
import smarthome.client.arduino.presentation.action.onoff.OnOffModelFactory
import smarthome.client.arduino.presentation.condition.ArduinoConditionModelResolver
import smarthome.client.arduino.presentation.condition.HumidityConditionModelFactory
import smarthome.client.arduino.presentation.condition.TemperatureConditionModelFactory
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

val arduino = module {
    // inject as plugin
    factory<ConditionFromBlockResolver>(named<ArduinoConditionFromBlockResolver>()) { ArduinoConditionFromBlockResolver() }
    factory<ControllerBlockResolver>(named<ArduinoControllerBlockResolver>()) { ArduinoControllerBlockResolver() }
    factory<ConditionModelResolver>(named<ArduinoConditionModelResolver>()) { ArduinoConditionModelResolver(get(), get()) }
    factory<ActionFromBlockResolver>(named<ArduinoActionFromBlockResolver>()) { ArduinoActionFromBlockResolver() }
    factory<ActionModelResolver>(named<ArduinoActionModelResolver>()) { ArduinoActionModelResolver(get()) }
    factory<BlockNameResolver>(named<ArduinoBlockNameResolver>()) { ArduinoBlockNameResolver(get()) }
    
    // internal
    factory { HumidityConditionModelFactory(get()) }
    factory { TemperatureConditionModelFactory(get()) }
    factory { OnOffModelFactory(get()) }
}