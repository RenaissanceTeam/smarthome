package smarthome.client.arduino.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.entity.condition.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.presentation.action.ArduinoActionModelResolver
import smarthome.client.arduino.presentation.action.ArduinoActionViewResolver
import smarthome.client.arduino.presentation.conditionview.ArduinoConditionModelResolver
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ActionViewResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

val arduino = module {
    factory<ConditionFromBlockResolver>(named<ArduinoConditionFromBlockResolver>()) { ArduinoConditionFromBlockResolver() }
    factory<ControllerBlockResolver>(named<ArduinoControllerBlockResolver>()) { ArduinoControllerBlockResolver() }
    factory<ConditionModelResolver>(named<ArduinoConditionModelResolver>()) { ArduinoConditionModelResolver() }
    factory<ActionFromBlockResolver>(named<ArduinoActionFromBlockResolver>()) { ArduinoActionFromBlockResolver() }
    factory<ActionViewResolver>(named<ArduinoActionViewResolver>()) { ArduinoActionViewResolver() }
    factory<ActionModelResolver>(named<ArduinoActionModelResolver>()) { ArduinoActionModelResolver() }
}