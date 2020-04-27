package smarthome.client.arduino.scripts.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.scripts.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.scripts.entity.block.ArduinoBlockNameResolver
import smarthome.client.arduino.scripts.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.scripts.entity.condition.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.scripts.presentation.ArduinoActionModelResolver
import smarthome.client.arduino.scripts.presentation.ArduinoConditionModelResolver
import smarthome.client.arduino.scripts.presentation.analog.AnalogConditionModelFactory
import smarthome.client.arduino.scripts.presentation.common.ReadActionModelFactory
import smarthome.client.arduino.scripts.presentation.dht.HumidityConditionModelFactory
import smarthome.client.arduino.scripts.presentation.dht.TemperatureConditionModelFactory
import smarthome.client.arduino.scripts.presentation.digital.DigitalConditionModelFactory
import smarthome.client.arduino.scripts.presentation.onoff.OnOffActionModelFactory
import smarthome.client.arduino.scripts.presentation.onoff.OnOffConditionModelFactory
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

val scriptsModule = module {
    // inject as plugin
    factory<ConditionFromBlockResolver>(named<ArduinoConditionFromBlockResolver>()) { ArduinoConditionFromBlockResolver() }
    factory<ControllerBlockResolver>(named<ArduinoControllerBlockResolver>()) { ArduinoControllerBlockResolver() }
    factory<ConditionModelResolver>(named<ArduinoConditionModelResolver>()) { ArduinoConditionModelResolver(
            humidityConditionModelFactory = get(),
            temperatureConditionModelFactory = get(),
            analogConditionModelFactory = get(),
            digitalConditionModelFactory = get(),
            onOffConditionModelFactory = get()
    ) }
    factory<ActionFromBlockResolver>(named<ArduinoActionFromBlockResolver>()) { ArduinoActionFromBlockResolver() }
    factory<ActionModelResolver>(named<ArduinoActionModelResolver>()) { ArduinoActionModelResolver(
            onOffActionModelFactory = get(),
            readActionModelFactory = get()
    ) }
    factory<BlockNameResolver>(named<ArduinoBlockNameResolver>()) { ArduinoBlockNameResolver(get()) }

    factory { HumidityConditionModelFactory(get()) }
    factory { TemperatureConditionModelFactory(get()) }
    factory { ReadActionModelFactory() }
    factory { AnalogConditionModelFactory(get()) }
    factory { DigitalConditionModelFactory(get()) }
    factory { OnOffActionModelFactory(get()) }
    factory { OnOffConditionModelFactory(get()) }
}